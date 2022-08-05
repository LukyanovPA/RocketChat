package com.pavellukyanov.rocketchat.presentation.feature.auth.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.databinding.FragmentSignInBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment() {
    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val vm: SignInViewModel by viewModels()

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