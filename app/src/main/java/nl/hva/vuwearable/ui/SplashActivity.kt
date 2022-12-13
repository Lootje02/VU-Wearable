package nl.hva.vuwearable.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.ActivitySplashscreenBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashscreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_splash)

//        setNavigation()
    }

//    private fun setNavigation(){
//        binding.btnSetup.setOnClickListener{
//        }
//
//        binding.btnDashboard.setOnClickListener{
//            navController.navigate(R.id.nav_host_fragment_activity_main)
//        }
//    }

}