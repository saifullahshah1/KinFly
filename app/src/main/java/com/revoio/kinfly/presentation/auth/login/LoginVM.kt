package com.revoio.kinfly.presentation.auth.login

import androidx.lifecycle.ViewModel
import com.revoio.kinfly.data.AuthRepo
import com.revoio.kinfly.utils.debug

class LoginVM : ViewModel() {
    private val tag = "LoginVM"
    private val repo = AuthRepo()

    fun loginUsingEmailPassword(email: String, password: String, callback: (Boolean) -> Unit) {
        "loginUsingEmailPassword:".debug(tag)
        // check internet connectivity
        // do input validations

        repo.loginUsingEmailPassword(
            email, password,
            callBack = { result ->
                if (result) callback(true) else callback(false)
            },
            error = { errorMsg ->
                "Error while login:\n$errorMsg".debug(tag)
                callback(false)
            })
    }

}