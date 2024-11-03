package com.example.todoapp.homescreen

import com.example.data.database.model.TaskEntity
import com.example.todoapp.edittask.UpdateTaskIntent

sealed class HomeIntent {

    data object LoadTasks : HomeIntent()

    data class MarkTaskAsDone(val taskEntity: TaskEntity) : HomeIntent()

    data class DeleteTaskById(val taskId: Long) : HomeIntent()

}