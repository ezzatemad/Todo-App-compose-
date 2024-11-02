package com.example.domain.taskrepo

import com.example.domain.model.Task


interface TaskRepo {

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(taskId: Long)

    suspend fun getAllTasks(): List<Task>

    suspend fun updateTask(task: Task)

    suspend fun selectedTaskByDate(date: Long): Task
}