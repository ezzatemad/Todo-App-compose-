package com.example.todoapp.addtaskbottomsheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.domain.usecase.InsertTaskUseCase
import com.example.todoapp.homescreen.HomeIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase,
    val channel: Channel<InsertIntent>,
    private val homeChannel: Channel<HomeIntent>
) : ViewModel() {

    private val _state = MutableStateFlow<InsertState>(InsertState.Idle)
    val state: StateFlow<InsertState> get() = _state.asStateFlow()


    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is InsertIntent.AddTask -> insertTask(
                        intent.title,
                        intent.description,
                        intent.date
                    )
                }
            }
        }
    }

    private fun insertTask(title: String, description: String, date: Long) {
        viewModelScope.launch {
            _state.value = InsertState.Loading
            try {
                val task = Task(title = title, description = description, date = date)
                insertTaskUseCase.insertTaskUseCase(task)
                _state.value = InsertState.Success(title, description, date)
                Log.d("InsertViewModel", "Inserted Task: $task")

                // Notify HomeViewModel to refresh tasks
                homeChannel.send(HomeIntent.LoadTasks)

            } catch (e: Exception) {
                _state.value = InsertState.Error("Failed to insert task: ${e.message}")
            }
        }
    }

}