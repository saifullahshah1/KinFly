package com.revoio.kinfly.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepo {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStoreRepo = FireStoreRepo()

    fun loginUsingEmailPassword(
        email: String,
        password: String,
        callBack: (Boolean) -> Unit,
        error: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { loginResponse ->
                if (loginResponse.isSuccessful) {
                    callBack(true)
                } else {
                    callBack(false)
                }
            }
            .addOnFailureListener { errorException ->
                error(errorException.message.toString())
            }
    }

    fun registerUser(
        userName: String, email: String, password: String,
        callBack: (Boolean, String?) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val tempContactList : List<String> = listOf()
                fireStoreRepo.saveUserData(
                    userName,
                    email,
                    tempContactList,
                    getCurrentUserId() ?: ""
                ) { response, error ->
                    if (response) {
                        callBack(true, null)
                    } else {
                        callBack(false, error)
                    }
                }
            }
            .addOnFailureListener { errorException ->
                callBack(false, errorException.message.toString())
            }
    }

    fun getCurrentLoggedUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    private fun getCurrentUserId(): String? {
        return getCurrentLoggedUser()?.uid
    }

    fun signOutUser(){
        firebaseAuth.signOut()
    }

}