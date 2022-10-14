package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.core.di.qualifiers.ChatUsersStorageQ
import com.pavellukyanov.rocketchat.domain.utils.ChatUserItemStorage
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import com.pavellukyanov.rocketchat.presentation.feature.chatroom.chat.item.ChatUserItem
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class StorageModule {
    @ChatUsersStorageQ
    @Singleton
    @Binds
    abstract fun bindChatUserItemStorage(impl: ChatUserItemStorage): ObjectStorage<List<ChatUserItem>>
}