package com.revoio.kinfly.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.FragmentHomeBinding
import com.revoio.kinfly.utils.debug
import com.revoio.kinfly.utils.setOnOneClickListener


class HomeFragment : Fragment() {

    private val tag = "HomeFragment"
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val homeVM = HomeVM()

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

    private fun handleClicks() {
        binding.sendMessageBtn.setOnOneClickListener{
            homeVM.sendMessage("Hello there!")
        }

        binding.signoutBtn.setOnOneClickListener {
            homeVM.signOut()
            if (findNavController().currentDestination?.id == R.id.homeFragment) {
                findNavController().popBackStack(R.id.onBoardingFragment,false)
            }
        }
    }

    private fun initViews() {
        homeVM.getCurrentUserDetails()?.let { currentUser ->
            binding.emailTV.text = currentUser.email
            binding.uIdTV.text = currentUser.uid
        }

        homeVM.getSimpleMessageReference().addSnapshotListener { snapshot: DocumentSnapshot?, e: FirebaseFirestoreException? ->
            if (e != null) {
                "Listen failed.".debug(tag)
                return@addSnapshotListener
            }else{
                if (snapshot != null && snapshot.exists()) {
                    val message = snapshot.getString("message")
                    "Message Listened Successfully\n$message".debug(tag)
                    message?.let { str ->
                        binding.messageTxt.text = str
                    }

                } else {
                    "Message Listened Current data: null".debug(tag)
                }
            }

        }

    }
}