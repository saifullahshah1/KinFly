package com.revoio.kinfly.presentation.auth.signup

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.core.CustomPasswordTransformationMethod
import com.revoio.kinfly.databinding.FragmentSignUpBinding
import com.revoio.kinfly.core.utils.debug
import com.revoio.kinfly.core.utils.showToast
import com.revoio.kinfly.databinding.DialogDataValidityBinding
import com.revoio.kinfly.presentation.dialog.DataValidityDialog
import com.revoio.kinfly.presentation.inputEditTextChangeListener
import com.revoio.kinfly.presentation.setOnOneClickListener

class SignUpFragment : Fragment() {

    private val tag = "SignUpFragment"
    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private val signupVM : SignupVM by activityViewModels()

    private val dataValidityDialog by lazy { Dialog(binding.root.context) }
    private val dataValidityDialogBinding by lazy { DialogDataValidityBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        handleClicks()
    }

    private fun initViews() {
        with(binding) {
            context?.let { ctx ->
                passwordItemV.itemIET.transformationMethod = CustomPasswordTransformationMethod('x')

                usernameItemV.itemIET.hint = ctx.getString(R.string.enter_full_name)
                emailItemV.itemIET.hint = ctx.getString(R.string.enter_email_address)
                numberItemV.itemIET.hint = ctx.getString(R.string.enter_phone_number)
                passwordItemV.itemIET.hint = ctx.getString(R.string.set_password)

                usernameItemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_username,
                        null
                    )
                )
                emailItemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_email,
                        null
                    )
                )
                numberItemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_phone_number,
                        null
                    )
                )
                passwordItemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_password_lock,
                        null
                    )
                )
            }
        }
    }

    private fun handleClicks() {
        with(binding) {

            inputEditTextChangeListener(
                usernameItemV,
                resources,
                R.drawable.ic_username_black,
                R.drawable.ic_username
            )
            inputEditTextChangeListener(
                emailItemV,
                resources,
                R.drawable.ic_email_black,
                R.drawable.ic_email
            )
            inputEditTextChangeListener(
                numberItemV,
                resources,
                R.drawable.ic_phone_number_black,
                R.drawable.ic_phone_number
            )
            inputEditTextChangeListener(
                passwordItemV,
                resources,
                R.drawable.ic_password_lock_black,
                R.drawable.ic_password_lock
            )


            termsConditionBtnRB.setOnOneClickListener {
                termsConditionBtnRB.isSelected = !termsConditionBtnRB.isSelected
                signupVM.termsAndConditionsChecked = !signupVM.termsAndConditionsChecked
            }

            context?.let { ctx ->
                activity?.let { aty ->
                    signupBtnTV.setOnClickListener {
                        signupVM.registerUsingEmailPassword(
                            ctx,
                            usernameItemV.itemIET.text.toString(),
                            emailItemV.itemIET.text.toString(),
                            numberItemV.itemIET.text.toString(),
                            passwordItemV.itemIET.text.toString(),
                            callback = { termsAndCondition, internetAvailable, res, error ->
                                if (termsAndCondition) {
                                    if (internetAvailable) {
                                        if (res) {
                                            showToast(ctx,"Email Verification Sent")
                                            if (findNavController().currentDestination?.id == R.id.signUpFragment) {
                                                findNavController().navigate(R.id.verifyEmailFragment)
                                            }
                                        } else {
                                            error.debug(tag)
                                            DataValidityDialog().showDataValidityDialog(
                                                aty,
                                                dataValidityDialog,
                                                dataValidityDialogBinding,
                                                ctx.getString(R.string.error_occurred),
                                                ctx.getString(R.string.wrong_password_or_email_kindly_enter_the_correct_password_and_email),
                                                R.drawable.ic_invalid_red,
                                                R.color.dull_red
                                            )
                                        }
                                    } else {
                                        DataValidityDialog().showDataValidityDialog(
                                            aty,
                                            dataValidityDialog,
                                            dataValidityDialogBinding,
                                            "No Internet",
                                            "You don't have an internet connection. Try connecting to one and then retry.",
                                            R.drawable.ic_warning_yellow,
                                            R.color.bright_yellow
                                        )
                                    }
                                } else {
                                    DataValidityDialog().showDataValidityDialog(
                                        aty,
                                        dataValidityDialog,
                                        dataValidityDialogBinding,
                                        "Terms & Conditions Not Agreed",
                                        "You have not agreed to out terms and conditions. In order to continue you have to agree to it.",
                                        R.drawable.ic_warning_yellow,
                                        R.color.bright_yellow
                                    )
                                }
                            })
                    }
                }
            }

            loginBtnTV.setOnOneClickListener {
                if (findNavController().currentDestination?.id == R.id.signUpFragment)
                    findNavController().popBackStack()
            }

        }
    }

}