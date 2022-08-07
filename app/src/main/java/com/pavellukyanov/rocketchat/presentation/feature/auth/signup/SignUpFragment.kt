package com.pavellukyanov.rocketchat.presentation.feature.auth.signup

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentSignUpBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class SignUpFragment : BaseFragment<SignUpViewModel>(
    SignUpViewModel::class.java,
    R.layout.fragment_sign_up
) {
    private val binding by viewBinding(FragmentSignUpBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.buttonState().observe(viewLifecycleOwner, ::handleButtonState)
        bind()
    }

    private fun bind() = with(binding) {
        regInputNickname.setOnTextChangeListener(vm::setNickname)
        regInputEmail.setOnTextChangeListener(vm::setEmail)
        regInputPassword.setOnTextChangeListener(vm::setPassword)
        regSignInLink.setOnClickListener { vm.forwardToSignIn() }
        regButton.setOnClickListener { vm.signUp() }
    }

    private fun handleButtonState(state: Boolean) {
        binding.regButton.isEnabled = state
    }

    companion object {
        fun newInstance(): SignUpFragment = SignUpFragment()

        val TAG = SignUpFragment::class.java.simpleName
    }
}