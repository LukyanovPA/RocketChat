package com.pavellukyanov.rocketchat.presentation.feature.chatroom.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavellukyanov.rocketchat.databinding.FragmentChatroomOptionsBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseBottomSheetDialogFragment

class ChatRoomOptionsFragment : BaseBottomSheetDialogFragment<FragmentChatroomOptionsBinding, ChatRoomOptionsViewModel>(
    ChatRoomOptionsViewModel::class.java
), ChatRoomOptionsAdapter.ChatRoomOptionsListener {
    private val optionsAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomOptionsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.start()
        bind()
        vm.items.observe(viewLifecycleOwner, ::handleOptions)
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentChatroomOptionsBinding {
        return FragmentChatroomOptionsBinding.inflate(inflater, container, false)
    }

    private fun bind() = with(binding) {
        chatroomOptionsList.apply {
            adapter = optionsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleOptions(options: List<OptionItem>) {
        optionsAdapter.data = options
    }

    override fun onItemClicked(item: OptionItem) {
        vm.onOptionClicked(item)
    }

    companion object {
        fun newInstance(): ChatRoomOptionsFragment = ChatRoomOptionsFragment()

        const val CHATROOM_OPTIONS_REQUEST_KEY = "CHATROOM_OPTIONS_REQUEST_KEY"
        val TAG = ChatRoomOptionsFragment::class.java.simpleName
    }
}