package com.revoio.kinfly.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.FragmentHomeBinding
import com.revoio.kinfly.presentation.setOnOneClickListener


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
            homeVM.sendMessageNew( binding.textmessageTIETxt.text.toString())
        }

        binding.signoutBtn.setOnOneClickListener {

// Stop listening for messages in all conversations when the user leaves the chat
            homeVM.stopListeningForMessagesInAllConversations()
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

// Start listening for new messages in all conversations for the user
        context?.let { ctx ->
            homeVM.getUserLatestMessages(ctx) { conversationId, newMessage ->
                println("New message in conversation $conversationId: $newMessage")
                binding.messageValTxt.text = "$conversationId: $newMessage"
                // You can also update your UI here to show the new message
            }
        }


        /*homeVM.getSimpleMessageReference()?.addSnapshotListener { snapshot: DocumentSnapshot?, e: FirebaseFirestoreException? ->
            if (e != null) {
                "Listen failed.".debug(tag)
                return@addSnapshotListener
            }else{
                if (snapshot != null && snapshot.exists()) {
                    val message = snapshot.getString("message_thread")
                    "Message Listened Successfully\n$message".debug(tag)
                    message?.let { str ->
                        binding.messageTxt.text = str
                    }  ?: "Null messages".debug(tag)

                } else {
                    "Message Listened Current data: null".debug(tag)
                }
            }

        }
*/
    }
}