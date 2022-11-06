package com.pavellukyanov.rocketchat.presentation

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.presentation.base.BaseNavigator
import com.pavellukyanov.rocketchat.presentation.base.State
import com.pavellukyanov.rocketchat.presentation.base.ViewModelFactory
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import com.pavellukyanov.rocketchat.presentation.helper.ResultWrapper
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    private val navigator by lazy(LazyThreadSafetyMode.NONE) { BaseNavigator(supportFragmentManager) }

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
        lifecycleScope.launch {
            vm.state.collect(::handleViewState)
        }
        lifecycleScope.launch {
            vm.effect.collect(::effect)
        }

        if (savedInstanceState == null) {
            vm.action(MainEvent.CheckAuth)
        }
    }

    private fun handleViewState(state: State<Any>) {
        when (state) {
            is State.Loading -> {}
            is State.Success -> {}
            is State.Error -> onError(state.error)
            is State.ErrorMessage -> navigator.showGlobalErrorDialog(state.errorMessage)
        }
    }

    private fun onError(error: Throwable) {
        when (error) {
            is ApiException.UnauthorizedException -> navigator.replace(SignInFragment.newInstance(), SignInFragment.TAG)
            else -> error.message?.let(navigator::showGlobalErrorDialog)
        }
    }

    private fun effect(effect: MainEffect) {
        when (effect) {
            is MainEffect.Home -> navigator.add(HomeFragment.newInstance(), HomeFragment.TAG)
            is MainEffect.SignIn -> navigator.add(SignInFragment.newInstance(), SignInFragment.TAG)
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}