package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.taskrepo.TaskRepo
import javax.inject.Inject

class GetAllTaskTaskUseCase @Inject constructor(private val taskRepo: TaskRepo) {


    suspend fun getAllTasksUseCase(): List<Task> {

        return taskRepo.getAllTasks()
    }
}