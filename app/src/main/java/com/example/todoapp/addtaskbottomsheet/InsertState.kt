package com.example.todoapp.addtaskbottomsheet

sealed class InsertState {

    data object Idle : InsertState()
    data object Loading : InsertState()
    data class Success(val title: String, val description: String, val date: Long) : InsertState()
    data class Error(val message: String) : InsertState()
}