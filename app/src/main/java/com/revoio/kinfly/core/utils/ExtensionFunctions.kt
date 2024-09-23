package com.revoio.kinfly.core.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.revoio.kinfly.databinding.ItemInputFieldBinding
import com.revoio.kinfly.presentation.activity.MainActivity


/** Text Validations */
fun emailValidation(email: String): Boolean {
    if (email.isBlank()) return false

    val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
    return emailRegex.matches(email)
}

fun passwordValidation(password: String): Boolean {
    if (password.length < 6) return false

    val letterRegex = "[a-zA-Z]".toRegex()
    val digitRegex = "\\d".toRegex()
    return letterRegex.containsMatchIn(password) && digitRegex.containsMatchIn(password)
}

fun phoneNumberValidation(phoneNumber: String): Boolean {
    val phoneRegex = "^(051\\d{6}|\\d{11}|\\+\\d{12})$".toRegex()
    return phoneRegex.matches(phoneNumber)
}

/** Internet Connectivity */
fun internetConnectivity(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

/** Log Function */
fun Any?.debug(tag: String = "Default_Tag") {
    Log.d(tag, "$this")
}

/** Toast Message */
fun showToast(context : Context, message : String){
    Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
}