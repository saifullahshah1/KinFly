package com.revoio.kinfly.presentation.auth.signup

import androidx.lifecycle.ViewModel
import com.revoio.kinfly.data.AuthRepo
import com.revoio.kinfly.utils.debug

class SignupVM : ViewModel() {

    private val tag = "SignupVM"
    private val repo = AuthRepo()

    fun registerUsingEmailPassword(userName : String ,email: String, password: String, callback: (Boolean, String?) -> Unit) {
        "registerUsingEmailPassword:".debug(tag)
        // check internet connectivity
        // do input validations

        repo.registerUser(userName, email, password) { res , error ->
                if (res)
                    callback(true,null)
                else
                    callback(false,error)
            }
    }

}