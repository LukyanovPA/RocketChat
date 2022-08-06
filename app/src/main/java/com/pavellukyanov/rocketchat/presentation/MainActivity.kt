package com.pavellukyanov.rocketchat.presentation

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.presentation.base.ViewModelFactory
import com.pavellukyanov.rocketchat.presentation.helper.ResultWrapper
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

    val permissionLauncher =
        ResultWrapper(
            activity = this,
            contract = ActivityResultContracts.RequestMultiplePermissions()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = ViewModelProvider(this, activityViewModelFactory)[MainViewModel::class.java]

        if (savedInstanceState == null) {
            vm.checkAuth()
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}