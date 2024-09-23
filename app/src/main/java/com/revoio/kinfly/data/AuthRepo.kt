package com.revoio.kinfly.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.revoio.kinfly.core.utils.debug
import com.revoio.kinfly.core.utils.showToast

class AuthRepo {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStoreRepo = FireStoreRepo()
    private val tag = "AuthRepo"

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
        email: String, password: String,
        callBack: (Boolean, String?) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                if (result.user?.isEmailVerified == false) {
                    result?.user?.reload()
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                firebaseAuth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener {
                                        callBack(true, null)
                                    }
                            } else {
                                callBack(false, it.exception?.message)
                            }
                        }
                } else {
                    callBack(false, "isEmailVerified Null")
                }
            }
            .addOnFailureListener { errorException ->
                callBack(false, errorException.message.toString())
            }
    }

    fun resendEmailVerification(callback: (Boolean, String?) -> Unit) {
        getCurrentLoggedUser()?.let { userObj ->
            userObj.sendEmailVerification().addOnCompleteListener {
                    callback(true, null)
                }.addOnFailureListener { err ->
                callback(true, err.message)
            }
        }
    }

    fun checkIsUserVerified(
        userName: String, email: String, phoneNumber: String,
        callback: (Boolean, String?) -> Unit
    ) {
        getCurrentLoggedUser()?.let { userObj ->
            userObj.reload()
            if (userObj.isEmailVerified) {
                val tempContactList: List<String> = listOf()
                fireStoreRepo.saveUserData(
                    userName,
                    email,
                    phoneNumber,
                    tempContactList,
                    getCurrentUserId() ?: ""
                ) { response, error ->
                    if (response) {
                        callback(true, null)
                    } else {
                        callback(false, error)
                    }
                }
            } else {
                callback(false, "Email still not verified")
            }
        }
    }

    fun deleteUnVerifiedUser(
        callback: (Boolean, String?) -> Unit
    ){
        getCurrentLoggedUser()?.let { userObj ->
            if (!userObj.isEmailVerified) {
                userObj.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            "User account deleted because email was not verified.".debug(tag)
                            callback(true,null)
                        } else {
                            task.exception?.let {
                                "Error deleting user: ${it.message}".debug(tag)
                            }
                            callback(false,task.exception?.message)
                        }
                    }
            } else {
                "User's email is verified. Not deleting the account.".debug(tag)
                callback(false,"Verified User")
            }
        } ?: run {
            "No user signed in.".debug(tag)
            callback(false,"No user logged in")
        }
    }

    fun getCurrentLoggedUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    private fun getCurrentUserId(): String? {
        return getCurrentLoggedUser()?.uid
    }

    fun signOutUser() {
        firebaseAuth.signOut()
    }

}