package com.example.todoapp.edittask

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.model.TaskEntity
import com.example.data.toEntity
import com.example.domain.model.Task
import com.example.domain.usecase.UpdateTaskUseCase
import com.example.todoapp.homescreen.HomeIntent
import com.example.todoapp.homescreen.HomeStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UpdateTaskViewModel @Inject constructor(
    private val updateTaskUseCase: UpdateTaskUseCase,
    val channel: Channel<UpdateTaskIntent>
) : ViewModel() {


    private val _state = MutableStateFlow<UpdateTaskStates>(UpdateTaskStates.Idle)
    val state: StateFlow<UpdateTaskStates> get() = _state.asStateFlow()


    init {
        handelIntent()
    }

    private fun handelIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is UpdateTaskIntent.updateTask -> updateTask(intent.taskEntity)
                }
            }
        }
    }

    private suspend fun updateTask(taskEntity: TaskEntity) {
        try {
            withContext(Dispatchers.IO) {
                updateTaskUseCase.updateTaskUseCase(
                    Task(taskEntity.id, taskEntity.title, taskEntity.description, taskEntity.date)
                )
            }
            _state.value = UpdateTaskStates.updateTasks(taskEntity)
        } catch (ex: Exception) {
            _state.value = UpdateTaskStates.Error(ex.localizedMessage ?: "Unknown error")
            Log.d("TAG", "updateTask : view model ${ex.localizedMessage}")
        }
    }


}