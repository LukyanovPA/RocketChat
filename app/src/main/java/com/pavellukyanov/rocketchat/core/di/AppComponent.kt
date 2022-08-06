package com.pavellukyanov.rocketchat.core.di

import android.content.Context
import com.pavellukyanov.rocketchat.core.app.App
import com.pavellukyanov.rocketchat.core.di.module.AppModule
import com.pavellukyanov.rocketchat.core.di.module.FirebaseModule
import com.pavellukyanov.rocketchat.core.di.module.RepositoryModule
import com.pavellukyanov.rocketchat.core.di.module.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        FirebaseModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}