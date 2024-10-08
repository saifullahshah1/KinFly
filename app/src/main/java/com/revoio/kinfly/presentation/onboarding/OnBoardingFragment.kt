package com.revoio.kinfly.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.FragmentOnBoardingBinding
import com.revoio.kinfly.presentation.setOnOneClickListener

class OnBoardingFragment : Fragment() {

    private val binding by lazy {
        FragmentOnBoardingBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleClicks()
    }

    private fun handleClicks() {
        binding.loginBtnTV.setOnOneClickListener {
            findNavController().navigate(R.id.logInFragment)
        }
        binding.nextBtnV.setOnOneClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
    }


}