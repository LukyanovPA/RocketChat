package com.pavellukyanov.rocketchat.presentation.feature.users.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavellukyanov.rocketchat.domain.entity.users.User
import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsers
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.users.UsersNavigator
import javax.inject.Inject

class ListUsersViewModel @Inject constructor(
    navigator: UsersNavigator,
    private val getAllUsers: GetAllUsers
) : BaseViewModel<UsersNavigator>(navigator) {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() = launchIO {
        getAllUsers().asState {
            _users.postValue(it)
        }
    }
}