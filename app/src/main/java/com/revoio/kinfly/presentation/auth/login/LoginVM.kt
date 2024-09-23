package com.revoio.kinfly.presentation.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.revoio.kinfly.data.AuthRepo
import com.revoio.kinfly.core.utils.debug
import com.revoio.kinfly.core.utils.emailValidation
import com.revoio.kinfly.core.utils.internetConnectivity
import com.revoio.kinfly.core.utils.passwordValidation

class LoginVM : ViewModel() {
    private val tag = "LoginVM"
    private val repo = AuthRepo()

    fun loginUsingEmailPassword(context : Context, email: String, password: String, callback: (Boolean, Boolean) -> Unit) {
        "loginUsingEmailPassword:".debug(tag)
        /** Internet Connected*/
        if(internetConnectivity(context)){

            /** Text Validations*/
            if(emailValidation(email)){
                if(passwordValidation(password)){

                    repo.loginUsingEmailPassword(
                        email, password,
                        callBack = { result ->
                            if (result) callback(true , true) else callback(true, false)
                        },
                        error = { errorMsg ->
                            "loginUsingEmailPassword: Login failed\n$errorMsg".debug(tag)
                            callback(true, false)
                        })

                }else{
                    "loginUsingEmailPassword: Password not valid".debug(tag)
                    callback(true, false)
                }
            }else{
                "loginUsingEmailPassword: Email not valid".debug(tag)
                callback(true, false)
            }
        }else{
            "loginUsingEmailPassword: Internet not connected".debug(tag)
            callback(false, false)
        }
    }

}