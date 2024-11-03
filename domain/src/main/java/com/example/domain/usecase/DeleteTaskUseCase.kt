package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.taskrepo.TaskRepo
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(val taskRepo: TaskRepo) {

    suspend fun deleteTaskUseCase(taskID: Long) {

        taskRepo.deleteTask(taskID)
    }
}