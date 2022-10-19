package com.pavellukyanov.rocketchat.presentation.feature.users.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pavellukyanov.rocketchat.databinding.ListItemUserBinding
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.presentation.base.BaseAdapter
import com.pavellukyanov.rocketchat.presentation.base.BaseViewHolder
import com.pavellukyanov.rocketchat.presentation.helper.ext.load

class ListUsersAdapter(
    usersListener: ListUsersListener
) : BaseAdapter<User>(usersListener) {

    interface ListUsersListener : BaseAdapterListener<User>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        ListUsersViewHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    class ListUsersViewHolder(override val binding: ListItemUserBinding) : BaseViewHolder(binding) {
        override fun bind(item: Any) {
            if (item is User) {
                with(binding) {
                    userAvatar.load(item.avatar, circleCrop = true)
                    userName.text = item.username
                }
            }
        }
    }
}