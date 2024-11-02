package com.example.domain.usecase

import com.example.domain.model.Task
import com.example.domain.taskrepo.TaskRepo
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(private val taskRepo: TaskRepo) {


    suspend fun insertTaskUseCase(task: Task) {

        taskRepo.insertTask(task)
    }
}