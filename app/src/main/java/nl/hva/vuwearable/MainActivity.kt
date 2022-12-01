package nl.hva.vuwearable

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.hva.vuwearable.databinding.ActivityMainBinding
import nl.hva.vuwearable.udp.UDPConnection
import nl.hva.vuwearable.ui.login.LoginViewModel
import nl.hva.vuwearable.ui.udp.UDPViewModel

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

        navView.setupWithNavController(navController)

        // Android does not allow to use a UDP socket on the main thread,
        // so we need to use it on a different thread
        Thread(UDPConnection {
            // Update the view model on the main thread
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.setIsConnected(it)
            }
        }).start()
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

    /**
     * @author Lorenzo Bindemann
     */
    private fun showDialog() {
        if (loginViewModel.isLoggedIn.value == false) {
            // create login pop up
            val dialogLayout = layoutInflater.inflate(R.layout.login_dialog, null)
            val builder = android.app.AlertDialog.Builder(this).setView(dialogLayout).show()

            // set login function on button click
            dialogLayout.findViewById<Button>(R.id.login_button).setOnClickListener {
                val inputCode =
                    dialogLayout.findViewById<EditText>(R.id.input_password).text.toString()
                loginViewModel.checkInput(inputCode, this@MainActivity)
                // check if login is successfully
                if (loginViewModel.isLoggedIn.value == true) {
                    builder.hide()
                    findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.professorDashboardFragment)
                } else {
                    // login is unsuccessfully
                    builder.findViewById<EditText>(R.id.input_password).setTextColor(Color.RED)
                    builder.findViewById<TextView>(R.id.wrong_password).visibility = View.VISIBLE
                }
            }
        } else {
            // create logout pop up
            val dialogLayout = layoutInflater.inflate(R.layout.logout_dialog, null)
            val builder = android.app.AlertDialog.Builder(this).setView(dialogLayout).show()

            // set logout function on button click
            dialogLayout.findViewById<Button>(R.id.logout_button).setOnClickListener {
                builder.hide()
                loginViewModel.setIsLoggedIn(false)
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_dashboard)
                Toast.makeText(this, getString(R.string.logout_successful), Toast.LENGTH_LONG)
                    .show()
            }
            // set cancel function on button click
            dialogLayout.findViewById<Button>(R.id.cancel_button).setOnClickListener {
                builder.hide()
            }
        }

        setupAppBar()
    }
}