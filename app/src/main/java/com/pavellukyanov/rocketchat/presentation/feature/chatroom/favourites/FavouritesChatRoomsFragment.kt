package com.pavellukyanov.rocketchat.presentation.feature.chatroom.favourites

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pavellukyanov.rocketchat.R
import com.pavellukyanov.rocketchat.databinding.FragmentFavouritesChatroomsBinding
import com.pavellukyanov.rocketchat.presentation.base.BaseFragment

class FavouritesChatRoomsFragment : BaseFragment<FavouritesChatRoomsViewModel>(
    FavouritesChatRoomsViewModel::class.java,
    R.layout.fragment_favourites_chatrooms
) {
    private val binding by viewBinding(FragmentFavouritesChatroomsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() = with(binding) {

    }

    companion object {
        fun newInstance(): FavouritesChatRoomsFragment = FavouritesChatRoomsFragment()

        val TAG = FavouritesChatRoomsFragment::class.java.simpleName
    }
}