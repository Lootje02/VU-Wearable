package nl.hva.vuwearable.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loginCode = "nextgen2022"
    val isLoggedIn = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun checkLogin (codeInput : String) {
        isLoggedIn.value = loginCode == codeInput
    }

}