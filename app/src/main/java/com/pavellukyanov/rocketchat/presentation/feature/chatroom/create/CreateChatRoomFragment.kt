package com.pavellukyanov.rocketchat.presentation.feature.chatroom.create

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentCreateChatroomBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.helper.ext.load
import com.pavellukyanov.rocketchat.presentation.helper.ext.setOnTextChangeListener

class CreateChatRoomFragment : BaseFragment<CreateChatRoomState, CreateChatRoomEvent, CreateChatRoomViewModel>(
    CreateChatRoomViewModel::class.java,
    R.layout.fragment_create_chatroom
) {
    private val binding by viewBinding(FragmentCreateChatroomBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {
        createChatroomArrowBack.setOnClickListener { action(CreateChatRoomEvent.GoBack) }
        addChatroomImage.setOnClickListener { action(CreateChatRoomEvent.ChangeImg) }
        createChatroomCheck.setOnClickListener { action(CreateChatRoomEvent.Create) }
        createChatroomName.setOnTextChangeListener { action(CreateChatRoomEvent.Name(it)) }
        createChatroomDescription.setOnTextChangeListener { action(CreateChatRoomEvent.Description(it)) }
    }

    override fun render(state: CreateChatRoomState) {
        when (state) {
            is CreateChatRoomState.Loading -> handleLoadingState(state.state)
            is CreateChatRoomState.Img -> handleChatroomImg(state.uri)
        }
    }

    private fun handleChatroomImg(uri: Uri) {
        binding.addChatroomImage.load(uri, circleCrop = true)
    }

    private fun handleLoadingState(state: Boolean) {
        binding.createChatRoomLoading.isVisible = state
    }

    companion object {
        fun newInstance(): CreateChatRoomFragment = CreateChatRoomFragment()

        val TAG = CreateChatRoomFragment::class.java.simpleName
    }
}