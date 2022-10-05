package com.pavellukyanov.rocketchat.core.di

import android.content.Context
import com.pavellukyanov.rocketchat.core.app.App
import com.pavellukyanov.rocketchat.core.di.module.*
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
        LocalBaseModule::class,
        ApiModule::class,
        GsonModule::class
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