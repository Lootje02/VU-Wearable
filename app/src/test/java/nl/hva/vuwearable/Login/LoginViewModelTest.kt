package nl.hva.vuwearable.Login

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.rules.activityScenarioRule
import nl.hva.vuwearable.MainActivity
import nl.hva.vuwearable.ui.login.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock

@RunWith(JUnit4::class)
class LoginViewModelTest {

    //Rule used for testing LiveData
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    //Rule used for testing activities
    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    // loginViewModel instance
    private lateinit var loginViewModel: LoginViewModel
    // mock application instance
    private val application = mock<Application>()

    @Before
    fun setUp() {
        //Initialize loginViewModel with mock application
        loginViewModel = LoginViewModel(application)
    }

    // Test method to check login with correct code
    @Test
    fun checkLogin_correctCode_isLoggedInIsTrue() {
        // Call check login with correct code
        loginViewModel.checkLogin("nextgen2022")
        // check if isLoggedIn value is true
        assertEquals(true, loginViewModel.isLoggedIn.value)
    }

    // Test method to check login with incorrect code
    @Test
    fun checkLogin_incorrectCode_isLoggedInIsFalse() {
        // Call check login with incorrect code
        loginViewModel.checkLogin("wrongcode")
        // check if isLoggedIn value is false
        assertEquals(false, loginViewModel.isLoggedIn.value)
    }

    // Test method to check user is logged in
    @Test
    fun checkIfUserIsLoggedIn_isLoggedInIsTrue_returnsTrue() {
        // Set isLoggedIn value to true
        loginViewModel.isLoggedIn.value = true
        // check if checkIfUserIsLoggedIn method returns true
        assertEquals(true, loginViewModel.checkIfUserIsLoggedIn())
    }

    // Test method to check user is not logged in
    @Test
    fun checkIfUserIsLoggedIn_isLoggedInIsFalse_returnsFalse() {
        // Set isLoggedIn value to false
        loginViewModel.isLoggedIn.value = false
        // check if checkIfUserIsLoggedIn method returns false
        assertEquals(false, loginViewModel.checkIfUserIsLoggedIn())
    }
}