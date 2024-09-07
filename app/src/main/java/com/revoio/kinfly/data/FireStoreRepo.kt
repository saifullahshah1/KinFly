package com.revoio.kinfly.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.revoio.kinfly.presentation.auth.MessageData
import com.revoio.kinfly.presentation.auth.UserData
import com.revoio.kinfly.utils.debug
import java.util.UUID

class FireStoreRepo {

    private val fireStoreObj : FirebaseFirestore = Firebase.firestore
    private lateinit var messageReceivingListener : ListenerRegistration

    fun saveUserData(username: String, email: String, userId: String, callBack: (Boolean, String?) -> Unit) {
        val userData = UserData(userId, email, username )
        val userDBRef = fireStoreObj.collection("users").document(userId)

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

    fun getSimpleMessageReference() : DocumentReference{
        "getSimpleMessageReference".debug("FireStoreRepo")
        return fireStoreObj.collection("simple_data").document("saif@gmail.com")
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

}