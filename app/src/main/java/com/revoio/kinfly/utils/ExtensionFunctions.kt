package com.revoio.kinfly.utils

import android.os.SystemClock
import android.util.Log
import android.view.View
import com.revoio.kinfly.presentation.MainActivity

/** Log Function */
fun Any?.debug(tag: String = "Default_Tag"){
    Log.d(tag, "$this")
}

/** Click Listener*/
fun View.setOnOneClickListener(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - MainActivity.lastClickTime < debounceTime) {
                return
            } else {
                action()
            }
            MainActivity.lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}