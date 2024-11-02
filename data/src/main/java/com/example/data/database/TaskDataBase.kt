package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.database.model.TaskEntity


@Database(entities = [TaskEntity::class], version = 1, exportSchema = true)
abstract class TaskDataBase : RoomDatabase() {

    abstract fun taskDao(): TaskDao


}