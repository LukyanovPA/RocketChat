package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentChatBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.hideKeyboard
import com.pavellukyanov.rocketchat.presentation.helper.ext.putArgs
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE

class ChatFragment : BaseFragment<ChatViewModel>(
    ChatViewModel::class.java,
    R.layout.fragment_chat
) {
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val chatAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        vm.messages.observe(viewLifecycleOwner, ::handleMessagesList)
        vm.buttonIsEnable().observe(viewLifecycleOwner, ::handleButtonSendState)
    }

    private fun bind() = with(binding) {
        messagesList.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            ).apply {
                stackFromEnd = true
            }
        }
        chatArrowBack.setOnClickListener { vm.back() }
        sendMessageEdtx.setOnTextChangeListener(vm::writeMessage)
        chatButtonSend.setOnClickListener {
            vm.sendMes()
            hideKeyboard()
            binding.sendMessageEdtx.text?.clear()
        }
    }

    private fun handleMessagesList(messages: List<ChatItem>) {
        chatAdapter.data = messages
        binding.messagesList.smoothScrollToPosition(messages.lastIndex + INT_ONE)
    }

    private fun handleButtonSendState(state: Boolean) {
        binding.chatButtonSend.isEnabled = state
    }

    companion object {
        fun newInstance(chatroomId: String): ChatFragment = ChatFragment().putArgs {
            putString(CHAT_ROOM_ID_ARG, chatroomId)
        }

        val TAG = ChatFragment::class.java.simpleName
        const val CHAT_ROOM_ID_ARG = "CHAT_ROOM_ID_ARG"
    }
}