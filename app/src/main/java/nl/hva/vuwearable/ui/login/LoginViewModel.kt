package nl.hva.vuwearable.ui.login

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.hva.vuwearable.R

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val LOGIN_CODE = "nextgen2022"
    private val APPLICATION = getApplication<Application>()
    val isLoggedIn = MutableLiveData(false)

    fun checkLogin (codeInput : String) {
        this.setIsLoggedIn(LOGIN_CODE == codeInput)
    }

    fun checkIfUserIsLoggedIn () : Boolean {
        return isLoggedIn.value == true
    }

    fun setIsLoggedIn (value : Boolean) {
        isLoggedIn.value = value
    }

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