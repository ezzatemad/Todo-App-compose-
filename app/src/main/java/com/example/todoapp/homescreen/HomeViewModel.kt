package com.example.todoapp.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.model.TaskEntity
import com.example.data.toDomainModel
import com.example.data.toEntity
import com.example.domain.model.Task
import com.example.domain.usecase.DeleteTaskUseCase
import com.example.domain.usecase.GetAllTaskTaskUseCase
import com.example.domain.usecase.UpdateTaskUseCase
import com.example.todoapp.edittask.UpdateTaskStates
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
class HomeViewModel @Inject constructor(
    private val getAllTaskTaskUseCase: GetAllTaskTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskUseCase,
    val channel: Channel<HomeIntent>
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> get() = _tasks.asStateFlow()

    private val _state = MutableStateFlow<HomeStates>(HomeStates.Idle)
    val state: StateFlow<HomeStates> get() = _state.asStateFlow()


    init {
        handelIntent()
    }

    private fun handelIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is HomeIntent.LoadTasks -> loadAllTasks()
                    is HomeIntent.MarkTaskAsDone -> updateTaskIsDone(intent.taskEntity)
                    is HomeIntent.DeleteTaskById -> deleteTaskById(intent.taskId)
                }
            }
        }
    }

    private fun deleteTaskById(taskId: Long) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    deleteTaskByIdUseCase.deleteTaskUseCase(taskId)
                }
                loadAllTasks() // Reload tasks here
                _state.value = HomeStates.DeleteTaskById(taskId) // Set state after reload
            } catch (ex: Exception) {
                _state.value = HomeStates.Error(ex.localizedMessage)
            }
        }
    }


    private fun updateTaskIsDone(task: TaskEntity) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // Convert TaskEntity to Task if necessary
                    val taskToUpdate = task.toDomainModel()
                    // Update the task in the database
                    updateTaskUseCase.updateTaskUseCase(taskToUpdate)
                    // Reload tasks to reflect the updated status
                    loadAllTasks()
                }
            } catch (e: Exception) {
                // Handle the error, maybe log it or update the UI state
                Log.e("HomeViewModel", "Error updating task: ${e.message}")
            }
        }
    }


    private suspend fun updateTask(taskEntity: TaskEntity) {

        _state.value = HomeStates.Loading

        try {
            val tasksList = updateTaskUseCase.updateTaskUseCase(
                Task(
                    taskEntity.id,
                    taskEntity.title,
                    taskEntity.description,
                    taskEntity.date
                )
            )

            _state.value = HomeStates.TaskIsDone(taskEntity)

            loadAllTasks()
        } catch (ex: Exception) {
            _state.value = HomeStates.Error(ex.localizedMessage)
        }

    }

    private suspend fun loadAllTasks() {
        _state.value = HomeStates.Loading

        try {
            val tasksList = getAllTaskTaskUseCase.getAllTasksUseCase()
            val taskEntity = tasksList.map {
                it.toEntity()
            }
            _tasks.value = taskEntity

            _state.value = HomeStates.getAllTasks(taskEntity)
        } catch (ex: Exception) {
            _state.value = HomeStates.Error(ex.localizedMessage)
        }
    }

}