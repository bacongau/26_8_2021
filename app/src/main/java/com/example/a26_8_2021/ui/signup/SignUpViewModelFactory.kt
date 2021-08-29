package com.example.a26_8_2021.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignUpViewModelFactory : ViewModelProvider.Factory {
    fun SignUpViewModelFactory() {}

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel() as T
//            return modelClass.getConstructor(Context::class.java).newInstance()
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}