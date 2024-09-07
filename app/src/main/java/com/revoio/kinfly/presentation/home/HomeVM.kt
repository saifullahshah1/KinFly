package com.revoio.kinfly.presentation.home

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

    fun getSimpleMessageReference() : DocumentReference{
        return dbRepo.getSimpleMessageReference()
    }

}