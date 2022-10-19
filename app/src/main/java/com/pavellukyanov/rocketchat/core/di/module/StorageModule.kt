package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.core.di.qualifiers.HomeSearchQ
import com.pavellukyanov.rocketchat.domain.usecase.home.HomeSearchQueryStorage
import com.pavellukyanov.rocketchat.domain.utils.ObjectStorage
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class StorageModule {
    @Singleton
    @Binds
    @HomeSearchQ
    abstract fun bindHomeSearchQueryStorage(impl: HomeSearchQueryStorage): ObjectStorage<String>
}