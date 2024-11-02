package com.example.todoapp.addtaskbottomsheet

sealed class InsertIntent {

    data class AddTask(val title: String, val description: String, val date: Long) : InsertIntent()

}