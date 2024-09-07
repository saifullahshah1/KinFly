package com.revoio.kinfly.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.revoio.kinfly.R
import com.revoio.kinfly.databinding.ActivityMainBinding
import com.revoio.kinfly.utils.debug

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
        var lastClickTime: Long = 0
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initConfig()
        handleListeners()
    }

    private fun initConfig() {

    }

    private fun handleListeners() {
        try {

            val navController = (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController

            navController.addOnDestinationChangedListener{ _, destination , _->
                "Nav Destination=> $destination".debug(TAG)
            }

        }catch (_ : Exception){}
    }


}