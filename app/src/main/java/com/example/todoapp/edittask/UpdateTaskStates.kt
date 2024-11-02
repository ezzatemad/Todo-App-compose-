package com.example.todoapp.edittask

import com.example.data.database.model.TaskEntity
import com.example.domain.model.Task
import com.example.todoapp.homescreen.HomeStates


sealed class UpdateTaskStates {

    data object Idle : UpdateTaskStates()
    data class updateTasks(val taskEntity: TaskEntity) : UpdateTaskStates()
    data class Error(val message: String) : UpdateTaskStates()

}