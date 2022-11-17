package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavellukyanov.rocketchat.databinding.FragmentChatroomOptionsBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseBottomSheetDialogFragment
import com.pavellukyanov.rocketchat.presentation.widget.SuccessEffect

class ChatRoomOptionsFragment :
    BaseBottomSheetDialogFragment<OptionsState, OptionsEvent, SuccessEffect, FragmentChatroomOptionsBinding, ChatRoomOptionsViewModel>(
        ChatRoomOptionsViewModel::class.java
    ), ChatRoomOptionsAdapter.ChatRoomOptionsListener {
    private val optionsAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomOptionsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        action(OptionsEvent.Start)
        bind()
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatroomOptionsBinding =
        FragmentChatroomOptionsBinding.inflate(inflater, container, false)

    private fun bind() = with(binding) {
        chatroomOptionsList.apply {
            adapter = optionsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun render(state: OptionsState) {
        optionsAdapter.data = state.list
    }

    override fun effect(effect: SuccessEffect) {
        navigator.back()
    }

    override fun onItemClicked(item: OptionItem) {
        action(OptionsEvent.Click(item))
    }

    companion object {
        fun newInstance(): ChatRoomOptionsFragment = ChatRoomOptionsFragment()

        const val CHATROOM_OPTIONS_REQUEST_KEY = "CHATROOM_OPTIONS_REQUEST_KEY"
        val TAG = ChatRoomOptionsFragment::class.java.simpleName
    }
}