package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentSignInBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class SignInFragment : BaseFragment<SignInViewModel>(
    SignInViewModel::class.java,
    R.layout.fragment_sign_in
) {
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.buttonState().observe(viewLifecycleOwner, ::handleButtonState)
        bind()
    }

    private fun bind() = with(binding) {
        loginInputEmail.setOnTextChangeListener(vm::setEmail)
        loginInputPassword.setOnTextChangeListener(vm::setPassword)
        loginSignUpLink.setOnClickListener { vm.forwardToSignUp() }
        loginButton.setOnClickListener { vm.signIn() }
    }

    private fun handleButtonState(state: Boolean) {
        binding.loginButton.isEnabled = state
    }

    companion object {
        fun newInstance(): SignInFragment = SignInFragment()

        val TAG = SignInFragment::class.java.simpleName
    }
}