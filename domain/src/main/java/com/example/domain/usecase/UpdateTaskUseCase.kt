package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.taskrepo.TaskRepo
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(val taskRepo: TaskRepo) {

    suspend fun updateTaskUseCase(task: Task) {

        taskRepo.updateTask(task)
    }
}