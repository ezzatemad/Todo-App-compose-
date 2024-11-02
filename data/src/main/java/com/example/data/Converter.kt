package com.example.data

import com.example.data.database.model.TaskEntity
import com.example.domain.model.Task


// Extension function to convert TaskEntity to Task
fun TaskEntity.toDomainModel(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date,
        isDone = this.isDone
    )
}

// Extension function to convert Task to TaskEntity
fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date,
        isDone = this.isDone
    )
}