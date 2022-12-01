package nl.hva.vuwearable

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.hva.vuwearable.databinding.ActivityMainBinding
import nl.hva.vuwearable.udp.UDPConnection
import nl.hva.vuwearable.ui.login.LoginViewModel
import nl.hva.vuwearable.ui.udp.UDPViewModel
import nl.hva.vuwearable.workmanager.BackgroundWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private val viewModel: UDPViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        setupAppBar()

        WorkManager.getInstance(applicationContext).enqueue(
            PeriodicWorkRequest.Builder(BackgroundWorker::class.java, 1, TimeUnit.MINUTES)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()
        )

        navView.setupWithNavController(navController)

        // Android does not allow to use a UDP socket on the main thread,
        // so we need to use it on a different thread
        Thread(UDPConnection({
            // Update the view model on the main thread
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.setIsConnected(it)
            }
        }, 3, 3)).start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_button -> showDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAppBar() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val dashboardId =
            if (loginViewModel.isLoggedIn.value == true) R.id.professorDashboardFragment
            else R.id.navigation_dashboard

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, dashboardId, R.id.navigation_chart
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun showDialog() {
        if (loginViewModel.isLoggedIn.value == false) {
            // Set up the input
            val input = EditText(this)
            // Specify the type of input expected
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            val builder = AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.login_to_professor))
                setView(input)
                setPositiveButton(getString(R.string.login), null)
                setNegativeButton(getString(R.string.cancel), null)
            }.show()

            val loginButton = builder.getButton(AlertDialog.BUTTON_POSITIVE)
            loginButton.setOnClickListener {
                loginViewModel.checkInput(input.text.toString(), this@MainActivity)
                if (loginViewModel.isLoggedIn.value == true) {
                    builder.hide()
                    findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.professorDashboardFragment)
                }
            }
        } else {
            val builder = AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.logout))
                setMessage(R.string.logout_description)
                setPositiveButton(getString(R.string.logout), null)
                setNegativeButton(getString(R.string.cancel), null)
            }.show()

            val logoutButton = builder.getButton(AlertDialog.BUTTON_POSITIVE)
            logoutButton.setOnClickListener {
                loginViewModel.setIsLoggedIn(false)
                builder.hide()
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_dashboard)
                Toast.makeText(this, getString(R.string.logout_successful), Toast.LENGTH_LONG)
                    .show()
            }
        }
        setupAppBar()
    }
}