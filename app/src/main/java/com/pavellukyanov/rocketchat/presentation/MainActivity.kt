package com.pavellukyanov.rocketchat.presentation

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.data.utils.errors.ApiException
import com.pavellukyanov.rocketchat.databinding.ActivityMainBinding
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
    private val binding by viewBinding(ActivityMainBinding::bind)

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
            vm.effect.collect(::handleAuth)
        }
    }

    private fun handleViewState(state: State<Any>) {
        binding.progressBar.isVisible = state is State.Loading
        when (state) {
            is State.Error -> onError(state.error)
            is State.ErrorMessage -> navigator.showGlobalErrorDialog(state.errorMessage)
            else -> {}
        }
    }

    private fun onError(error: Throwable) {
        when (error) {
            is ApiException.UnauthorizedException -> navigator.add(SignInFragment.newInstance(), SignInFragment.TAG)
            else -> error.message?.let(navigator::showGlobalErrorDialog)
        }
    }

    private fun handleAuth(state: Boolean) {
        if (state) navigator.add(HomeFragment.newInstance(), HomeFragment.TAG) else navigator.add(
            SignInFragment.newInstance(),
            SignInFragment.TAG
        )
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}