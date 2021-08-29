package com.example.a26_8_2021.base

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.a26_8_2021.custom.LoadingDialog

abstract class BaseActivity<VM : BaseViewModel, T : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var viewModel: VM
    protected lateinit var binding: T
    protected lateinit var loadingDialog: LoadingDialog
    protected lateinit var actionBar: ActionBar

    protected abstract fun createViewModel(): VM;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar = supportActionBar!!
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        viewModel = createViewModel()
        loadingDialog = LoadingDialog(this)
        initView()
        initData()
        initListener()
    }

    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()
}