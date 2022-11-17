package nl.hva.vuwearable.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel() : ViewModel() {
    private val loginCode = "nextgen2022"
    val isLoggedIn = MutableLiveData(false)

    fun checkLogin (codeInput : String) {
        isLoggedIn.value = loginCode == codeInput
    }

}