package com.revoio.kinfly.presentation.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.FragmentSignUpBinding
import com.revoio.kinfly.utils.debug

class SignUpFragment : Fragment() {

    private val tag = "SignUpFragment"
    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private val signupVM = SignupVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        handleClicks()
    }

    private fun initViews() {

    }

    private fun handleClicks() {
        with(binding) {
            signupBtn.setOnClickListener {
                signupVM.registerUsingEmailPassword(
                    userNameIET.text.toString(),
                    emailIET.text.toString(),
                    passwordIET.text.toString(),
                    callback = { res , error ->
                        if (res) {
                            if (findNavController().currentDestination?.id == R.id.signUpFragment) {
                                findNavController().navigate(R.id.homeFragment)
                            }
                        }else{
                            error.debug(tag)
                        }
                    })
            }
        }
    }

}