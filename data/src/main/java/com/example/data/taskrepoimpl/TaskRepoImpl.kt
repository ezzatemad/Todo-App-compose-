package com.example.data.taskrepoimpl

import android.util.Log
import androidx.room.Insert
import com.example.data.database.TaskDao
import com.example.data.database.model.TaskEntity
import com.example.data.toDomainModel
import com.example.data.toEntity
import com.example.domain.model.Task
import com.example.domain.taskrepo.TaskRepo
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepo {


    override suspend fun insertTask(task: Task) {

        val taskEntity = task.toEntity()
        taskDao.insert(taskEntity)
        Log.d("TAG", "insertTask: $task")
    }

    override suspend fun deleteTask(taskId: Long) {
        taskDao.deleteById(taskId)
    }

    override suspend fun getAllTasks(): List<Task> {
        val tasksList = taskDao.getAllTasks()

        return tasksList.map {
            it.toDomainModel()
        }

    }

    override suspend fun updateTask(task: Task) {
        val taskEntity = task.toEntity()
        taskDao.update(taskEntity)
    }

    override suspend fun selectedTaskByDate(date: Long): Task {

        val task = taskDao.selectTaskByDate(date)

        return task.toDomainModel()
    }

}