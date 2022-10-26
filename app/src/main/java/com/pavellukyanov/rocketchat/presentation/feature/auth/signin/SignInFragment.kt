package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentSignInBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class SignInFragment : BaseFragment<Boolean, SignInEvent, SignInViewModel>(
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
        loginSignUpLink.setOnClickListener { action(SignInEvent.GoToSignUp) }
        loginButton.setOnClickListener { action(SignInEvent.SignIn) }
    }

    override fun render(state: Boolean) {
        binding.loginButton.isEnabled = state
    }

    companion object {
        fun newInstance(): SignInFragment = SignInFragment()

        val TAG = SignInFragment::class.java.simpleName
    }
}