package com.example.todoapp

import com.example.domain.taskrepo.TaskRepo
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetAllTaskTaskUseCase
import com.example.domain.usecase.InsertTaskUseCase
import com.example.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object di {


    @Provides
    fun provideDeleteTaskById(taskRepo: TaskRepo): DeleteTaskUseCase {
        return DeleteTaskUseCase(taskRepo)
    }


    @Provides
    fun provideUpdateTasks(taskRepo: TaskRepo): UpdateTaskUseCase {
        return UpdateTaskUseCase(taskRepo)
    }

    @Provides
    fun provideGetAllTasks(taskRepo: TaskRepo): GetAllTaskTaskUseCase {
        return GetAllTaskTaskUseCase(taskRepo)
    }

    @Provides
    fun provideInsertTaskUseCase(taskRepo: TaskRepo): InsertTaskUseCase {
        return InsertTaskUseCase(taskRepo)
    }


}