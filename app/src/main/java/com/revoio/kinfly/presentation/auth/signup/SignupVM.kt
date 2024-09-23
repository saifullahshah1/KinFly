package com.revoio.kinfly.presentation.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import com.revoio.kinfly.data.AuthRepo
import com.revoio.kinfly.core.utils.debug
import com.revoio.kinfly.core.utils.emailValidation
import com.revoio.kinfly.core.utils.internetConnectivity
import com.revoio.kinfly.core.utils.passwordValidation
import com.revoio.kinfly.core.utils.phoneNumberValidation
import kotlinx.coroutines.flow.callbackFlow

class SignupVM : ViewModel() {

    private val tag = "SignupVM"
    private val repo = AuthRepo()

    private var _username = ""
    var username
        get() = _username
        set(value) {
            _username = value
        }

    private var _email = ""
    var email
        get() = _email
        set(value) {
            _email = value
        }

    private var _number = ""
    var number
        get() = _number
        set(value) {
            _number = value
        }

    private var _password = ""
    var password
        get() = _password
        set(value) {
            _password = value
        }

    private var _termsAndConditionsChecked = false
    var termsAndConditionsChecked
        get() = _termsAndConditionsChecked
        set(value) {
            _termsAndConditionsChecked = value
        }

    fun registerUsingEmailPassword(
        context: Context,
        userName: String,
        email: String,
        phoneNumber: String,
        password: String,
        callback: (Boolean, Boolean, Boolean, String?) -> Unit
    ) {
        "registerUsingEmailPassword:".debug(tag)
        if (_termsAndConditionsChecked) {
            /** Internet Connected*/
            if (internetConnectivity(context)) {

                /** Text Validations*/
                if (emailValidation(email)) {
                    if (passwordValidation(password)) {
                        if (phoneNumberValidation(phoneNumber)) {
                            _username = userName
                            _email = email
                            _number = phoneNumber
                            _password = password

                            repo.registerUser(email, password) { res, error ->
                                if (res)
                                    callback(true, true, true, null)
                                else
                                    callback(true, true, false, error)
                            }
                        } else {
                            "registerUsingEmailPassword: Phone Number Not Valid".debug(tag)
                            callback(true, true, false, null)
                        }
                    } else {
                        "registerUsingEmailPassword: Password Not Valid".debug(tag)
                        callback(true, true, false, null)
                    }
                } else {
                    "registerUsingEmailPassword: Email Not Valid".debug(tag)
                    callback(true, true, false, null)
                }
            } else {
                "registerUsingEmailPassword: Internet not connected".debug(tag)
                callback(true, false, false, null)
            }
        } else {
            "registerUsingEmailPassword: Terms And Conditions Not Selected".debug(tag)
            callback(false, false, false, null)
        }

    }

    fun checkEmailVerified(callback: (Boolean, String?) -> Unit) {
        repo.checkIsUserVerified(_username, _email, _number) { res, err ->
            if (res) {
                callback(true, null)
            } else {
                callback(false, err)
            }
        }
    }

    fun reVerifyUser(callback: (Boolean, String?) -> Unit) {
        repo.resendEmailVerification() { res, err ->
            if (res) {
                callback(true, null)
            } else {
                callback(false, err)
            }
        }
    }

    fun deleteUnVerifiedUser(callback: (Boolean, String?) -> Unit){
        repo.deleteUnVerifiedUser() { res, err ->
            if (res) {
                callback(true, null)
            } else {
                callback(false, err)
            }
        }
    }


}