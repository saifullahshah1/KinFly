package com.revoio.kinfly.presentation.auth.login

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.core.CustomPasswordTransformationMethod
import com.revoio.kinfly.databinding.FragmentLogInBinding
import com.revoio.kinfly.databinding.ItemInputFieldBinding
import com.revoio.kinfly.presentation.dialog.DataValidityDialog
import com.revoio.kinfly.databinding.DialogDataValidityBinding
import com.revoio.kinfly.presentation.inputEditTextChangeListener
import com.revoio.kinfly.presentation.setOnOneClickListener

class LogInFragment : Fragment() {

    private val binding by lazy { FragmentLogInBinding.inflate(layoutInflater) }
    private val loginVM = LoginVM()

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

                emailItemV.itemIET.hint = ctx.getString(R.string.enter_email_address)
                passwordItemV.itemIET.hint = ctx.getString(R.string.enter_password)

                emailItemV.itemIV.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_email,
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

            inputEditTextChangeListener(emailItemV, resources, R.drawable.ic_email_black, R.drawable.ic_email)
            inputEditTextChangeListener(passwordItemV, resources, R.drawable.ic_password_lock_black, R.drawable.ic_password_lock)

            context?.let { ctx ->
                activity?.let { aty ->
                    loginBtnTV.setOnClickListener {
                        loginVM.loginUsingEmailPassword(
                            ctx,
                            emailItemV.itemIET.text.toString(),
                            passwordItemV.itemIET.text.toString(),
                            callback = { internetAvailable, result ->
                                if (internetAvailable) {
                                    if (result) {
                                        if (findNavController().currentDestination?.id == R.id.logInFragment) {
                                            findNavController().navigate(R.id.homeFragment)
                                        }
                                    } else {
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
                            })
                    }
                }
            }

            binding.signupBtnTV.setOnOneClickListener {
                if (findNavController().currentDestination?.id == R.id.logInFragment)
                    findNavController().navigate(R.id.signUpFragment)
            }
        }
    }


}