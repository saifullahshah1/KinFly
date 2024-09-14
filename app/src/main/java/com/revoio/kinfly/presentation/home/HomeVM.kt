package com.revoio.kinfly.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.revoio.kinfly.data.AuthRepo
import com.revoio.kinfly.data.FireStoreRepo
import com.revoio.kinfly.presentation.auth.MessageData

class HomeVM : ViewModel() {

    private val repo = AuthRepo()
    private val dbRepo = FireStoreRepo()

    fun getCurrentUserDetails(): FirebaseUser? {
        return repo.getCurrentLoggedUser()
    }

    fun sendMessage(message: String) {
        dbRepo.sendSimpleData(message)
    }

    fun getMessage() {
        dbRepo.getSimpleMessage()
    }

    fun getSimpleMessageReference(): DocumentReference? {
        repo.getCurrentLoggedUser()?.uid?.let { senderId ->
           return dbRepo.getSimpleMessageReference(senderId)
        }
        return null }

    fun signOut() {
        repo.signOutUser()
    }

    fun sendMessageNew(messageText: String) {
        repo.getCurrentLoggedUser()?.uid?.let { senderId ->
            // Get the contacts (friends) of the current user
            dbRepo.getUserContacts(senderId) { contacts ->
                if (contacts != null) {
                    println("Contacts for $senderId: $contacts")
                    // Now you can select a contact to send a message to
                    val friendUserId = contacts.firstOrNull() // Pick the first friend for example
                    if (friendUserId != null) {
                        // Send a message to the selected friend
                        dbRepo.sendMessage(senderId, friendUserId, messageText)
                    }
                } else {
                    println("No contacts found or failed to retrieve contacts.")
                }
            }
        }
    }

    fun getUserLatestMessages( context: Context, onNewMessage: (String, String) -> Unit){
        getCurrentUserDetails()?.uid?.let {
//            dbRepo.getUserMessages(it)

            dbRepo.listenForNewMessagesInAllConversations(it, context){ id ,mes_res,  ->
                onNewMessage(id,mes_res)
            }
        }
    }

    fun stopListeningForMessagesInAllConversations(){
        dbRepo.stopListeningForMessagesInAllConversations()
    }

}