package com.pavellukyanov.rocketchat.presentation.feature.home

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.usecase.home.RefreshChatroomsCache
import com.pavellukyanov.rocketchat.domain.usecase.home.UpdateCurrentUser
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.domain.utils.UserInfo
import com.pavellukyanov.rocketchat.presentation.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userInfo: UserInfo,
    private val refreshChatroomsCache: RefreshChatroomsCache,
    @HomeSearchQ private val searchStorage: ObjectStorage<String>,
    private val updateCurrentUser: UpdateCurrentUser
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>() {
    override val initialCurrentSuccessState: HomeState = HomeState()

    override var curState: HomeState = HomeState()

    override fun action(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetMyAccount -> fetchMyAccount()
            is HomeEvent.RefreshCache -> refreshCache()
            is HomeEvent.Search -> search(event.query)
            is HomeEvent.GoToMyProfile -> sendEffect(HomeEffect.GoToProfile)
            is HomeEvent.CreateChat -> sendEffect(HomeEffect.CreateChat)
        }
    }

    private fun search(query: String) = launchCPU {
        searchStorage.setObject(query)
    }

    private fun fetchMyAccount() = launchIO {
        updateCurrentUser.invoke()
        val newState = currentSuccessState.value.copy(
            myAccount = userInfo.user
        )
        newState.myAccount?.let { reduce(newState) }
    }

    private fun refreshCache() = launchIO {
        refreshChatroomsCache.invoke().collect {}
    }
}