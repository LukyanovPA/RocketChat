package com.pavellukyanov.rocketchat.presentation.feature.users.list

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsers
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class ListUsersViewModel @Inject constructor(
    private val getAllUsers: GetAllUsers,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>
) : BaseViewModel<List<User>, UsersEvent, UsersEffect>() {

    override fun action(event: UsersEvent) {
        when (event) {
            is UsersEvent.UserOnClick -> sendEffect(UsersEffect.ForwardToUserProfile(event.user.uuid))
            is UsersEvent.FetchUsers -> fetchUsers()
        }
    }

    @OptIn(FlowPreview::class)
    private fun fetchUsers() = launchIO {
        searchStorage.observ
            .flatMapMerge { query ->
                getAllUsers(query)
            }
            .collect(::emitState)
    }
}