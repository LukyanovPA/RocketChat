package com.pavellukyanov.rocketchat.presentation.feature.users.list

import com.pavellukyanov.rocketchat.domain.usecase.users.GetAllUsers
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import com.pavellukyanov.rocketchat.presentation.feature.users.UsersNavigator
import javax.inject.Inject

class ListUsersViewModel @Inject constructor(
    navigator: UsersNavigator,
    private val getAllUsers: GetAllUsers
) : BaseViewModel<UsersNavigator>(navigator) {

}