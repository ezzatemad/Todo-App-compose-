package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.database.model.TaskEntity

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TaskEntity)

    @Query("DELETE FROM tasks where id = :taskId")
    suspend fun deleteById(taskId: Long)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks where date = :date")
    suspend fun selectTaskByDate(date: Long): TaskEntity

    @Update
    fun update(task: TaskEntity)
}