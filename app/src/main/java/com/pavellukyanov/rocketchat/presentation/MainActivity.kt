package com.pavellukyanov.rocketchat.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.base.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var activityViewModelFactory: ViewModelFactory<MainViewModel>

    lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = ViewModelProvider(this, activityViewModelFactory)[MainViewModel::class.java]

        if (savedInstanceState == null) {
            vm.checkAuth()
            vm.testAuth().observe(this, ::testCheck)
        }
    }

    private fun testCheck(str: String) {
        if (str.isNotEmpty()) {
            Toast.makeText(this, "Логин заебиська!", Toast.LENGTH_LONG).show()
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}