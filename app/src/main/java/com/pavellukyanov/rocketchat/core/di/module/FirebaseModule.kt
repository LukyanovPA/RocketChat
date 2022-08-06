package com.pavellukyanov.rocketchat.core.di.module

import com.pavellukyanov.rocketchat.data.firebase.AuthFirebase
import com.pavellukyanov.rocketchat.data.firebase.AuthFirebaseImpl
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebase
import com.pavellukyanov.rocketchat.data.firebase.StorageFirebaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class FirebaseModule {
    @Binds
    @Singleton
    abstract fun bindAuthFirebase(impl: AuthFirebaseImpl): AuthFirebase

    @Binds
    @Singleton
    abstract fun bindStorageFirebase(impl: StorageFirebaseImpl): StorageFirebase
}