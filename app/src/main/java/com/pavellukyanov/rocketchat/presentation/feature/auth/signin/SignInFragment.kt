package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentSignInBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.AuthState
import com.pavellukyanov.rocketchat.presentation.feature.auth.signup.SignUpFragment
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener
import com.pavellukyanov.rocketchat.presentation.widget.SuccessEffect

class SignInFragment : BaseFragment<AuthState, SignInEvent, SuccessEffect, SignInViewModel>(
    SignInViewModel::class.java,
    R.layout.fragment_sign_in
) {
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {
        loginInputEmail.setOnTextChangeListener { action(SignInEvent.Email(it)) }
        loginInputPassword.setOnTextChangeListener { action(SignInEvent.Password(it)) }
        loginSignUpLink.setOnClickListener { forwardToSignUp() }
        loginButton.setOnClickListener { action(SignInEvent.SignIn) }
    }

    private fun forwardToSignUp() {
        navigator.forward(SignUpFragment.newInstance(), SignUpFragment.TAG)
    }

    override fun render(state: AuthState) {
        binding.loginButton.isEnabled = state.state
    }

    override fun effect(effect: SuccessEffect) {
        navigator.replace(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    companion object {
        fun newInstance(): SignInFragment = SignInFragment()

        val TAG = SignInFragment::class.java.simpleName
    }
}