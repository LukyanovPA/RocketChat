package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentCreateChatroomBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class CreateChatroomFragment : BaseFragment<CreateChatroomViewModel>(
    CreateChatroomViewModel::class.java,
    R.layout.fragment_create_chatroom
) {
    private val binding by viewBinding(FragmentCreateChatroomBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        vm.chatroomImg.observe(viewLifecycleOwner, ::handleChatroomImg)
    }

    private fun bind() = with(binding) {
        createChatroomArrowBack.setOnClickListener { vm.back() }
        addChatroomImage.setOnClickListener { vm.changeChatroomImg() }
        createChatroomCheck.setOnClickListener { vm.createChatroom() }
        createChatroomName.setOnTextChangeListener(vm::setChatroomName)
        createChatroomDescription.setOnTextChangeListener(vm::setChatroomDescription)
    }

    private fun handleChatroomImg(uri: Uri) {
        binding.addChatroomImage.load(uri, circleCrop = true)
    }

    companion object {
        fun newInstance(): CreateChatroomFragment = CreateChatroomFragment()

        val TAG = CreateChatroomFragment::class.java.simpleName
    }
}