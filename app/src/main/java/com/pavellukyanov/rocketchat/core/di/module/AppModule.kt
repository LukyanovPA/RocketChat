package com.pavellukyanov.rocketchat.core.di.module

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.pavellukyanov.rocketchat.presentation.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class AppModule {
//    @Binds
//    abstract fun provideMainActivity(): MainActivity

//    @Module
//    @InstallIn(SingletonComponent::class)
//    companion object {
//    @Inject
//    lateinit var activity: MainActivity

    @ActivityScoped
    @Provides
    fun provideSupportFragmentManager(@ActivityContext context: Context): FragmentManager =
        (context as MainActivity).supportFragmentManager
//    }
}