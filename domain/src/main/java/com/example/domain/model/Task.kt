package com.example.domain.model

data class Task(

    val id: Long? = null,

    val title: String? = null,

    val description: String? = null,

    val date: Long? = null,

    val isDone: Boolean? = false
)
