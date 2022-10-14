package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentChatBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseWebSocketFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.adapters.ChatAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.adapters.ChatUsersAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import com.pavellukyanov.rocketchat.presentation.helper.ext.hideKeyboard
import com.pavellukyanov.rocketchat.presentation.helper.ext.putArgs
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import timber.log.Timber

class ChatFragment : BaseWebSocketFragment<ChatViewModel>(
    ChatViewModel::class.java,
    R.layout.fragment_chat
), ChatAdapter.ChatListener {
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val chatAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatAdapter(this) }
    private val chatUsersAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatUsersAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phMessageList)
        bind()
        vm.messages.observe(viewLifecycleOwner, ::handleMessagesList)
        vm.buttonIsEnable().observe(viewLifecycleOwner, ::handleButtonSendState)
        vm.chatroomValue.observe(viewLifecycleOwner, ::handleChatroomValue)
        vm.users.observe(viewLifecycleOwner, ::handleUsersAvatars)
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
        chatUsersList.apply {
            adapter = chatUsersAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
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

    override fun adapterIsVisible(isVisible: Boolean) {
        if (isVisible) viewIsLoad()
    }

    override fun onItemClicked(item: ChatItem) {
        Timber.d("Smotrim $item")
    }

    private fun handleUsersAvatars(users: List<ChatUserItem>) {
        chatUsersAdapter.data = users
    }

    private fun handleButtonSendState(state: Boolean) {
        binding.chatButtonSend.isEnabled = state
    }

    private fun handleChatroomValue(chatroom: Chatroom?) = with(binding) {
        chatName.text = chatroom?.name
    }

    companion object {
        fun newInstance(chatroom: Chatroom? = null): ChatFragment = ChatFragment().putArgs {
            putParcelable(CHAT_ROOM_ID_ARG, chatroom)
        }

        val TAG = ChatFragment::class.java.simpleName
        const val CHAT_ROOM_ID_ARG = "CHAT_ROOM_ID_ARG"
    }
}