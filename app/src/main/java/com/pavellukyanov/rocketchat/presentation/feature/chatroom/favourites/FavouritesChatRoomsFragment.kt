package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentFavouritesChatroomsBinding
import com.pavellukyanov.rocketchat.domain.entity.chatroom.Chatroom
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chatrooms.ChatRoomsAdapter

class FavouritesChatRoomsFragment : ChatRoomsAdapter.ChatRoomListener, BaseFragment<FavouritesChatRoomsViewModel>(
    FavouritesChatRoomsViewModel::class.java,
    R.layout.fragment_favourites_chatrooms
) {
    private val binding by viewBinding(FragmentFavouritesChatroomsBinding::bind)
    private val chatroomAdapter by lazy(LazyThreadSafetyMode.NONE) { ChatRoomsAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShimmer(binding.phFavouritesList)
        bind()
        vm.chatrooms.observe(viewLifecycleOwner, ::handleChatroomList)
    }

    private fun bind() = with(binding) {
        favouritesList.apply {
            adapter = chatroomAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun handleChatroomList(listChatroom: List<Chatroom>) {
        chatroomAdapter.data = listChatroom
    }

    override fun onItemClicked(item: Chatroom) {
        vm.forwardToChatroom(item)
    }

    override fun adapterIsVisible(isVisible: Boolean) {
        if (isVisible) vm.stopShimmer()
    }

    companion object {
        fun newInstance(): FavouritesChatRoomsFragment = FavouritesChatRoomsFragment()

        val TAG = FavouritesChatRoomsFragment::class.java.simpleName
    }
}