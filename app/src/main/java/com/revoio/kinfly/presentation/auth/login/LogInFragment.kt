package com.revoio.kinfly.presentation.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {

    private val binding by lazy { FragmentLogInBinding.inflate(layoutInflater) }
    private val loginVM = LoginVM()

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

    }

    private fun handleClicks() {
        with(binding){
            loginBtn.setOnClickListener {
                loginVM.loginUsingEmailPassword(emailIET.text.toString(),passwordIET.text.toString(),
                    callback = { res ->
                        if(res){
                            if(findNavController().currentDestination?.id == R.id.logInFragment){
                                findNavController().navigate(R.id.homeFragment)
                            }
                        }
                    })
            }
        }
    }


}