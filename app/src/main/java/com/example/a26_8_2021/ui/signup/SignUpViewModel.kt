package com.example.a26_8_2021.ui.signup

import android.util.Log
import android.util.Patterns
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.example.a26_8_2021.base.BaseViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpViewModel() : BaseViewModel() {
    private val TAG = "SignUpViewModel"

    private var email = MutableLiveData<String>("")
    private var password = MutableLiveData<String>("")

    private var formatEmail = MutableLiveData<Int>()

    fun getEmail(): MutableLiveData<String> {
        return email
    }

    fun getPassword(): MutableLiveData<String> {
        return password
    }

    fun getFormatEmail(): MutableLiveData<Int> {
        return formatEmail
    }


}