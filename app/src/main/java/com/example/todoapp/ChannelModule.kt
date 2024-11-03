package com.example.todoapp

import com.example.todoapp.homescreen.HomeIntent
import com.example.todoapp.addtaskbottomsheet.InsertIntent
import com.example.todoapp.edittask.UpdateTaskIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.channels.Channel


@Module
@InstallIn(ViewModelComponent::class)
class ChannelModule {


    @Provides
    fun provideUpdateTaskIntentChannel(): Channel<UpdateTaskIntent> {
        return Channel(Channel.UNLIMITED)
    }

    @Provides
    fun provideHomeIntentChannel(): Channel<HomeIntent> {
        return Channel(Channel.UNLIMITED)
    }

    @Provides
    fun provideInsertTaskIntentChannel(): Channel<InsertIntent> {
        return Channel(Channel.UNLIMITED)
    }
}