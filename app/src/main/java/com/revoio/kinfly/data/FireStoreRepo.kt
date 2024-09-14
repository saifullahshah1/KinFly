package com.revoio.kinfly.data

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.revoio.kinfly.presentation.auth.MessageData
import com.revoio.kinfly.presentation.auth.UserData
import com.revoio.kinfly.presentation.widget.MainWidget
import com.revoio.kinfly.utils.debug
import java.util.UUID

class FireStoreRepo {

    private val fireStoreObj : FirebaseFirestore = Firebase.firestore
    private lateinit var messageReceivingListener : ListenerRegistration

    fun saveUserData(username: String, email: String, contacts : List<String>,  userId: String, callBack: (Boolean, String?) -> Unit) {
        val userData = UserData(userId, email, username , contacts)
        val userDBRef = fireStoreObj.collection("user").document(userId)

        userDBRef.set(userData).addOnSuccessListener {
            callBack(true, null)
        }.addOnFailureListener {  errorException ->
            callBack(false, errorException.message.toString())
        }
    }

    fun sendSimpleData(message: String){
        val messageData = MessageData(message)
        val randomID = UUID.randomUUID().toString()
        val dbRef = fireStoreObj.collection("simple_data").document("saif@gmail.com")
        dbRef.set(messageData).addOnSuccessListener {
           "message sent success".debug("FireStoreRepo")
        }.addOnFailureListener {
            "message sent failed".debug("FireStoreRepo")
        }
    }

    fun getSimpleMessageReference(senderId : String) : DocumentReference{
        "getSimpleMessageReference".debug("FireStoreRepo")
        return fireStoreObj.collection("messages").document(senderId)
    }

    fun getSimpleMessage() {
        "getSimpleMessage".debug("FireStoreRepo")
        val uidRef = fireStoreObj.collection("simple_data").document("saif@gmail.com")
//        uidRef.get().addOnSuccessListener { doc ->
//            if (doc != null) {
//                val user = doc.toObject(MessageData::class.java)
//                "${user?.message}".debug("FireStoreRepo")
//            } else {
//                "No such document".debug("FireStoreRepo")
//            }
//        }.addOnFailureListener { exception ->
//            "get failed with ".debug("FireStoreRepo")
//        }
        // Listen for changes in the document
        messageReceivingListener  =  uidRef.addSnapshotListener { snapshot: DocumentSnapshot?, e: FirebaseFirestoreException? ->
           if (e != null) {
               // Handle error
               Log.w("Firestore", "Listen failed.", e)
               return@addSnapshotListener
           }

           if (snapshot != null && snapshot.exists()) {
               // Successfully received data
               val message = snapshot.getString("message")

               Log.d("Firestore", "Current data: $message ")

           } else {
               // Document does not exist
               Log.d("Firestore", "Current data: null")
           }
       }

    }

    fun removeMessageReceiver(){
        messageReceivingListener.remove()
    }

    // Function to send a message
    fun sendMessage( senderId: String, receiverId: String ,messageText: String ) {

        // Step 1: Find/Create a conversation between two users (unique for both users)
        val participants = listOf(senderId, receiverId).sorted() // Sorting to ensure the conversation is unique for both

        val messagesRef = fireStoreObj.collection("messages")
        val query = messagesRef.whereArrayContains("participants", senderId).get()

        query.addOnSuccessListener { querySnapshot ->
            var conversationExists = false
            var conversationId: String? = null

            // Check if the conversation already exists between the two users
            for (document in querySnapshot.documents) {
                val conversationParticipants = document.get("participants") as List<String>
                if (conversationParticipants.contains(receiverId)) {
                    conversationExists = true
                    conversationId = document.id
                    break
                }
            }

            if (conversationExists) {
                // Conversation exists, send the message to the existing conversation
                saveMessage(conversationId!!, senderId, messageText)
            } else {
                // Step 2: Create a new conversation if it doesn't exist
                val newConversation = hashMapOf(
                    "participants" to participants,
/*
                    "timestamp" to Timestamp.now()
*/
                )

                messagesRef.add(newConversation).addOnSuccessListener { documentReference ->
                    conversationId = documentReference.id
                    saveMessage(conversationId!!, senderId, messageText)

                    // Step 3: Update the 'chats_list' for both users with the new conversation ID
                    updateUserChatsList(senderId, conversationId!!)
                    updateUserChatsList(receiverId, conversationId!!)
                }
            }
        }
    }

    // Function to save the message to the conversation thread
    fun saveMessage(conversationId: String, senderId: String, messageText: String) {
        val messageData = hashMapOf(
            "content" to messageText,
            "senderId" to senderId,
            "timestamp" to Timestamp.now() // Ensure we are storing a timestamp
        )

        fireStoreObj.collection("messages")
            .document(conversationId)
            .collection("message_thread")
            .add(messageData)
            .addOnSuccessListener {
                println("Message sent successfully")
            }
            .addOnFailureListener {
                println("Failed to send message: ${it.message}")
            }
    }

    // Function to update the 'chats_list' for a user
    fun updateUserChatsList(userId: String, conversationId: String) {
        fireStoreObj.collection("chats").document(userId)
            .update("chats_list", FieldValue.arrayUnion(conversationId))
            .addOnSuccessListener {
                println("Chats list updated successfully for user: $userId")
            }
            .addOnFailureListener {
                println("Failed to update chats list: ${it.message}")
            }
    }



    // Function to get the user's contacts (friends)
    fun getUserContacts(userId: String, onResult: (List<String>?) -> Unit) {
        // Access the "user" collection and fetch the document for the given userId
        fireStoreObj.collection("user")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Extract the 'contact_list' which contains the userIds of friends
                    val contactList = documentSnapshot.get("contacts") as? List<String>
                    // Call the result callback with the contact list
                    onResult(contactList)
                } else {
                    println("User document not found")
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                println("Failed to retrieve user contacts: ${exception.message}")
                onResult(null)
            }
    }

    val messageListeners = mutableListOf<ListenerRegistration?>() // To track multiple listeners
    // Function to listen for new messages across all conversations for a user
    fun listenForNewMessagesInAllConversations(userId: String, context : Context,onNewMessage: (String, String) -> Unit) {
        // Step 1: Get the user's chat list from the "chats" collection
        fireStoreObj.collection("chats").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val chatsList = documentSnapshot.get("chats_list") as? List<String>
                if (!chatsList.isNullOrEmpty()) {
                    // Step 2: Set up listeners for each conversation in the chat list
                    chatsList.forEach { conversationId ->
                        listenForNewMessagesInConversation(conversationId, context) { newMessage ->
                            // Call the callback function with the conversation ID and message
                            onNewMessage(conversationId, newMessage)
                        }
                    }
                } else {
                    println("No conversations found for user.")
                }
            }
            .addOnFailureListener {
                println("Failed to retrieve chats list: ${it.message}")
            }
    }

    // Function to listen for new messages in a specific conversation
    fun listenForNewMessagesInConversation(conversationId: String, context: Context, onNewMessage: (String) -> Unit) {
        // Step 3: Set up a listener for the "message_thread" subcollection of the conversation
        val listener = fireStoreObj.collection("messages")
            .document(conversationId)
            .collection("message_thread")
            .orderBy("timestamp", Query.Direction.ASCENDING) // Order by time (ASCENDING ensures correct order)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    println("Error listening for new messages: ${firebaseFirestoreException.message}")
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    println("Received update in conversation $conversationId with ${querySnapshot.documents.size} documents.")

                    // Step 4: Check for new messages in document changes
                    for (change in querySnapshot.documentChanges) {
                        if (change.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                            val newMessage = change.document.getString("content")
                            println("New message added: $newMessage")
                            if (newMessage != null) {
                                // Notify about the new message
                                sendWidgetUpdateBroadcast(context, newMessage)
                                onNewMessage(newMessage)
                            }
                        }
                    }
                } else {
                    println("No changes in conversation $conversationId")
                }
            }

        // Track the listener to remove later if necessary
        messageListeners.add(listener)
    }
    // Send a broadcast to update the widget
    private fun sendWidgetUpdateBroadcast(context: Context, message: String) {
        val intent = Intent(context, MainWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra("new_message", message)
        }
        // Send the broadcast to update the widget
        context.sendBroadcast(intent)
    }



    // Function to stop listening for messages in all conversations
    fun stopListeningForMessagesInAllConversations() {
        // Remove all listeners
        for (listener in messageListeners) {
            listener?.remove()
        }
        messageListeners.clear()
    }


}