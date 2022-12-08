package nl.hva.vuwearable.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.ActivitySplashscreenBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    private val navController = findNavController(R.id.splashFragment2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splashscreen)
    }

}