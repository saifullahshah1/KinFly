package com.revoio.kinfly.presentation.dialog

import android.app.Activity
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.revoio.kinfly.databinding.DialogDataValidityBinding
import com.revoio.kinfly.presentation.setOnOneClickListener

class DataValidityDialog {

    private var dataValidityDialog : Dialog? = null

    // do provide internet not available dialog

    fun showDataValidityDialog(
        activity: Activity,
        dataValidityDialog: Dialog,
        dataValidityDialogBinding: DialogDataValidityBinding,
        dialogTitle : String,
        dialogDesc : String,
        dialogIndicator : Int,
        dialogBottomColor : Int,
    ) {
        dataValidityDialog.setContentView(dataValidityDialogBinding.root)
        dataValidityDialog.setCancelable(false)

        with(dataValidityDialogBinding) {
            dataValidityTitleTV.text = dialogTitle
            dataValidityDescTV.text = dialogDesc
            dataValidityIV.setImageDrawable(ResourcesCompat.getDrawable(activity.resources,dialogIndicator,null))
            dataValidityV.setBackgroundColor(ContextCompat.getColor(activity,dialogBottomColor))

            cancelIV.setOnOneClickListener {
                if (dataValidityDialog.isShowing) {
                    dataValidityDialog.dismiss()
                }
            }
        }
        dataValidityDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dataValidityDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dataValidityDialog.window?.setGravity(Gravity.BOTTOM)

        if (!activity.isDestroyed && !activity.isFinishing)
            dataValidityDialog.show()

    }
}