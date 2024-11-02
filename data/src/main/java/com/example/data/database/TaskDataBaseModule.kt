package com.example.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class TaskDataBaseModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TaskDataBase::class.java, "TaskDB")
            .build()

    @Provides
    fun provideTaskDao(taskDataBase: TaskDataBase) =
        taskDataBase.taskDao()
}
