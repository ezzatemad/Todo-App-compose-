package com.example.todoapp.homescreen

import com.example.data.database.model.TaskEntity
import com.example.domain.model.Task


sealed class HomeStates {
    data object Idle : HomeStates()
    data object Loading : HomeStates()
    data class getAllTasks(val tasksList: List<TaskEntity>) : HomeStates()
    data class TaskIsDone(val taskEntity: TaskEntity) : HomeStates()
    data class Error(val message: String) : HomeStates()

}