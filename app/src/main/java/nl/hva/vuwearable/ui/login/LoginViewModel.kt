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
    val isLoggedIn = MutableLiveData(false)

    fun checkLogin (codeInput : String) {
        this.setIsLoggedIn(LOGIN_CODE == codeInput)
    }

    fun setIsLoggedIn (value : Boolean) {
        isLoggedIn.value = value
    }

    fun checkInput ( inputText : String, context : Context) {

        val toastText = if (inputText.isEmpty()) {
            getApplication<Application>().getString(R.string.input_is_empty)
        } else {
            checkLogin(inputText)
            if (isLoggedIn.value == true) {
                getApplication<Application>().getString(R.string.login_successful)
            } else {
                getApplication<Application>().getString(R.string.incorrect_code)
            }
        }
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
    }

}