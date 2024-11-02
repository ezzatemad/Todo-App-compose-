package com.example.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val title: String? = null,

    val description: String? = null,

    val date: Long? = null,

    val isDone: Boolean? = false

)
