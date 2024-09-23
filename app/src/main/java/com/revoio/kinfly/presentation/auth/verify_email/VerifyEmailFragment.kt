package com.revoio.kinfly.presentation.auth.verify_email

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.core.utils.debug
import com.revoio.kinfly.core.utils.showToast
import com.revoio.kinfly.databinding.DialogDataValidityBinding
import com.revoio.kinfly.databinding.FragmentVerifyEmailBinding
import com.revoio.kinfly.presentation.auth.signup.SignupVM
import com.revoio.kinfly.presentation.dialog.DataValidityDialog
import com.revoio.kinfly.presentation.setOnOneClickListener

class VerifyEmailFragment : Fragment() {

    private val tag = "VerifyEmailFragment"

    private val binding by lazy { FragmentVerifyEmailBinding.inflate(layoutInflater) }
    private val signupVM: SignupVM by activityViewModels()

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
        configureBackPress{}
        handleClicks()
    }



    private fun handleClicks() {
        with(binding) {
            context?.let { ctx ->
                activity?.let { aty ->
                    doneBtnTV.setOnOneClickListener {
                        signupVM.checkEmailVerified { res, err ->
                            if (res) {
                                if (findNavController().currentDestination?.id == R.id.verifyEmailFragment) {
                                    showToast(ctx, "Email Verified")
                                    findNavController().navigate(R.id.homeFragment)
                                }
                            } else {
                                err.debug(tag)
                                DataValidityDialog().showDataValidityDialog(
                                    aty,
                                    dataValidityDialog,
                                    dataValidityDialogBinding,
                                    "Unverified Email",
                                    "This email is not verified. Kindly verify it.",
                                    R.drawable.ic_warning_yellow,
                                    R.color.bright_yellow
                                )
                            }
                        }
                    }

                    resendBtnTV.setOnOneClickListener {
                        signupVM.reVerifyUser { res, err ->
                            if (res) {
                                showToast(ctx, "Verification email sent!")
                            } else {
                                err.debug(tag)
                                DataValidityDialog().showDataValidityDialog(
                                    aty,
                                    dataValidityDialog,
                                    dataValidityDialogBinding,
                                    "Failed to Send Email",
                                    "Verification email failed to send. Kindly try again by resending the verification email.",
                                    R.drawable.ic_invalid_red,
                                    R.color.dull_red
                                )
                            }
                        }
                    }
                }

            }

        }
    }

    override fun onDestroyView() {
        "onDestroyView".debug(tag)
        super.onDestroyView()
    }

    override fun onDestroy() {
        "onDestroy".debug(tag)
        super.onDestroy()
    }

    private fun configureBackPress(callbackResult : () -> Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callbackResult()
            }
        }

        try {
            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
        } catch (_: IllegalStateException) {
        } catch (_: java.lang.Exception) {
        } catch (_: Exception) {
        }
    }
}