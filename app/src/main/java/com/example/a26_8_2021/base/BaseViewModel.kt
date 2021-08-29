package com.example.a26_8_2021.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private var loading = MutableLiveData<Boolean>()

    fun getLoading(): MutableLiveData<Boolean> = loading

}