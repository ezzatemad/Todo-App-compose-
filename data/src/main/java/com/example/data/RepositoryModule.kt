package com.example.data

import com.example.data.taskrepoimpl.TaskRepoImpl
import com.example.domain.taskrepo.TaskRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideInsertTaskRepository(taskRepoImpl: TaskRepoImpl): TaskRepo

}