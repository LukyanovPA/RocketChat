package com.pavellukyanov.rocketchat.presentation.feature.auth.signup

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentSignUpBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.auth.AuthState
import com.pavellukyanov.rocketchat.presentation.feature.auth.signin.SignInFragment
import com.pavellukyanov.rocketchat.presentation.feature.home.HomeFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class SignUpFragment : BaseFragment<AuthState, SignUpEvent, SignUpViewModel>(
    SignUpViewModel::class.java,
    R.layout.fragment_sign_up
) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {
        regInputNickname.setOnTextChangeListener { action(SignUpEvent.Nickname(it)) }
        regInputEmail.setOnTextChangeListener { action(SignUpEvent.Email(it)) }
        regInputPassword.setOnTextChangeListener { action(SignUpEvent.Password(it)) }
        regSignInLink.setOnClickListener { forwardToSignIn() }
        regButton.setOnClickListener { action(SignUpEvent.SignUp) }
    }

    private fun forwardToSignIn() {
        navigator.forward(SignInFragment.newInstance(), SignInFragment.TAG)
    }

    override fun render(state: AuthState) {
        when (state) {
            is AuthState.ButtonState -> binding.regButton.isEnabled = state.state
            is AuthState.Success -> navigator.forward(HomeFragment.newInstance(), HomeFragment.TAG)
        }
    }

    companion object {
        fun newInstance(): SignUpFragment = SignUpFragment()

        val TAG = SignUpFragment::class.java.simpleName
    }
}