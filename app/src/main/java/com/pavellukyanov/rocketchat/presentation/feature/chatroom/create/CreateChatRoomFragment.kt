package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentCreateChatroomBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.base.SimpleDialogFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class CreateChatRoomFragment : BaseFragment<CreateChatRoomState, CreateChatRoomEvent, CreateChatEffect, CreateChatRoomViewModel>(
    CreateChatRoomViewModel::class.java,
    R.layout.fragment_create_chatroom
) {
    private val binding by viewBinding(FragmentCreateChatroomBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {
        createChatroomArrowBack.setOnClickListener { navigator.back() }
        addChatroomImage.setOnClickListener { action(CreateChatRoomEvent.ChangeImg) }
        createChatroomCheck.setOnClickListener { action(CreateChatRoomEvent.Create) }
        createChatroomName.setOnTextChangeListener { action(CreateChatRoomEvent.Name(it)) }
        createChatroomDescription.setOnTextChangeListener { action(CreateChatRoomEvent.Description(it)) }
    }

    override fun render(state: CreateChatRoomState) {
        state.uri?.let { binding.addChatroomImage.load(it, circleCrop = true) }
        if (state.isEmptyName) showEmptyChatroomNameErrorDialog()
    }

    override fun effect(effect: CreateChatEffect) {
        binding.createChatRoomLoading.isVisible = effect is CreateChatEffect.Loading
        if (effect is CreateChatEffect.Success) navigator.back()
    }

    private fun showEmptyChatroomNameErrorDialog() {
        navigator.showDialog(
            SimpleDialogFragment.newInstance(
                titleRes = R.string.global_error_title,
                messageRes = R.string.create_chatroom_empty_name_error,
                closeButtonRes = R.string.global_error_button_close
            ), SimpleDialogFragment.TAG
        )
    }

    companion object {
        fun newInstance(): CreateChatRoomFragment = CreateChatRoomFragment()

        val TAG = CreateChatRoomFragment::class.java.simpleName
    }
}