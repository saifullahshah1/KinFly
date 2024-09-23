package com.revoio.kinfly.presentation.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.ActivityMainBinding
import com.revoio.kinfly.core.utils.debug
import com.revoio.kinfly.presentation.auth.signup.SignupVM

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        var lastClickTime: Long = 0
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val signupVM: SignupVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handleListeners()
    }

    private fun handleListeners() {
        try {
            val navController = (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
            navController.addOnDestinationChangedListener { _, destination, _ ->
                "Nav Destination=> $destination".debug(TAG)
            }
        } catch (_: Exception) {}
    }

    override fun onDestroy() {
        "OnDestory".debug(TAG)
//        signupVM.deleteUnVerifiedUser { res, err ->
//                    if(res){
//                        "UnVerified User Deleted".debug(TAG)
//                    }else{
//                        err.debug(TAG)
//                    }
//                }
        super.onDestroy()
    }

}