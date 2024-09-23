package com.revoio.kinfly.presentation

import android.app.Activity
import android.content.res.Resources
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.revoio.kinfly.databinding.ItemInputFieldBinding
import com.revoio.kinfly.presentation.activity.MainActivity


/** Input Edit Text Listener */
fun inputEditTextChangeListener(
    itemV: ItemInputFieldBinding,
    resources : Resources,
    drawableValue: Int,
    drawableValueDefault: Int
) {
    itemV.itemIET.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (itemV.itemIET.text?.isEmpty() == true) {
                itemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        drawableValueDefault,
                        null
                    )
                )
            } else {
                itemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        drawableValue,
                        null
                    )
                )
            }
        }

        override fun afterTextChanged(p0: Editable?) {}
    })
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

/** BackPress */
