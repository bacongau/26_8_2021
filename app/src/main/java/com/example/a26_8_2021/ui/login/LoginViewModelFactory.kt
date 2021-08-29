package com.example.a26_8_2021.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory : ViewModelProvider.Factory {
    fun LoginViewModelFactory() {}

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
//            return modelClass.getConstructor(Context::class.java).newInstance()
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}