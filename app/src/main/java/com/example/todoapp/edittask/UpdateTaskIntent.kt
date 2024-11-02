package com.example.todoapp.edittask

import com.example.data.database.model.TaskEntity

sealed class UpdateTaskIntent {

    data class updateTask(val taskEntity: TaskEntity) : UpdateTaskIntent()
}