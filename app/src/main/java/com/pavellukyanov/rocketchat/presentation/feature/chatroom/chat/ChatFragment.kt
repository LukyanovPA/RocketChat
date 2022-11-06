package com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentChatBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.adapters.ChatAdapter
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatItem
import com.pavellukyanov.rocketchat.presentation.helper.ext.hideKeyboard
import com.pavellukyanov.rocketchat.presentation.helper.ext.putArgs
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener
import com.pavellukyanov.rocketchat.utils.Constants.INT_ONE
import timber.log.Timber

class ChatFragment : BaseFragment<ChatState, ChatEvent, ChatViewModel>(
    ChatViewModel::class.java,
    R.layout.fragment_chat
), ChatAdapter.ChatListener {
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val chatAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phMessageList)
        bind()
        (requireArguments().getParcelable(CHAT_ROOM_ID_ARG) as? Chatroom)?.let {
            handleChatroomValue(it)
        }
        lifecycleScope.launchWhenCreated {
            vm.effect.collect {
                if (it is ChatEffect.Back) navigator.back()
            }
        }
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
        chatArrowBack.setOnClickListener { action(ChatEvent.GoBack) }
        sendMessageEdtx.setOnTextChangeListener { action(ChatEvent.Message(it)) }
        chatButtonSend.setOnClickListener {
            action(ChatEvent.SendMessage)
            hideKeyboard()
            binding.sendMessageEdtx.text?.clear()
        }
        chatroomIsFavourites.setOnClickListener { action(ChatEvent.ChangeFavourites) }
    }

    override fun render(state: ChatState) {
        when (state) {
            is ChatState.Messages -> handleMessagesList(state.messages)
            is ChatState.ChatValue -> handleChatroomValue(state.chatRoom)
            is ChatState.ButtonState -> handleButtonSendState(state.state)
//            is ChatState.Back -> navigator.back()
        }
    }

    private fun handleMessagesList(messages: List<ChatItem>) {
        chatAdapter.data = messages
        binding.messagesList.smoothScrollToPosition(messages.lastIndex + INT_ONE)
    }

    override fun onItemClicked(item: ChatItem) {
        Timber.d("Smotrim $item")
    }

    private fun handleButtonSendState(state: Boolean) {
        binding.chatButtonSend.isEnabled = state
    }

    private fun handleChatroomValue(chatroom: Chatroom) = with(binding) {
        chatName.text = chatroom.name
        chatDescription.text = chatroom.description
        chatroomIsFavourites.setImageResource(if (chatroom.isFavourites) R.drawable.ic_is_favourites else R.drawable.ic_is_not_favourites)
    }

    companion object {
        fun newInstance(chatroom: Chatroom? = null): ChatFragment = ChatFragment().putArgs {
            putParcelable(CHAT_ROOM_ID_ARG, chatroom)
        }

        val TAG = ChatFragment::class.java.simpleName
        const val CHAT_ROOM_ID_ARG = "CHAT_ROOM_ID_ARG"
    }
}