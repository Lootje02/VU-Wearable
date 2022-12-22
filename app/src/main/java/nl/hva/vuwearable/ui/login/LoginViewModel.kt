package nl.hva.vuwearable.ui.login

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import nl.hva.vuwearable.R

/**
 * This viewmodel is made to do the login from a participant to a researcher
 * With this viewmodel you can reach the researcher features like a professor dashboard
 * @author Lorenzo Bindemann
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val LOGIN_CODE = "nextgen2022"
    private val APPLICATION = getApplication<Application>()
    val isLoggedIn = MutableLiveData(false)

    /**
     * Function to check if the ccorrect password is filled in
     * @author Lorenzo Bindemann
     */
    fun checkLogin (codeInput : String) {
        this.setIsLoggedIn(LOGIN_CODE == codeInput)
    }

    fun checkIfUserIsLoggedIn () : Boolean {
        return isLoggedIn.value == true
    }

    fun setIsLoggedIn (value : Boolean) {
        isLoggedIn.value = value
    }

    /**
     * Function to check if the input is filled in correctly
     * Also send a message to the user for a status update
     * @author Lorenzo Bindemann
     */
    fun checkInput ( inputText : String, context : Context) {

        val toastText = if (inputText.isEmpty()) {
            APPLICATION.getString(R.string.input_is_empty)
        } else {
            checkLogin(inputText)
            if (checkIfUserIsLoggedIn()) {
                APPLICATION.getString(R.string.login_successful)
            } else {
                APPLICATION.getString(R.string.incorrect_code)
            }
        }
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
    }

}