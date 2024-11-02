package com.example.todoapp.edittask

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.database.model.TaskEntity
import com.example.todoapp.Dimen
import com.example.todoapp.R
import com.example.todoapp.edittask.ui.theme.ToDoAppTheme
import com.example.todoapp.homescreen.HomeScreen
import com.example.todoapp.setting.SettingScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Retrieve the data
            val id = intent.getLongExtra("TASK_ID", 0)
            val taskTitle = intent.getStringExtra("TASK_TITLE")
            val taskDescription = intent.getStringExtra("TASK_DESCRIPTION")
            val taskDate = intent.getStringExtra("TASK_DATE")

            MainScreen(id, taskTitle.toString(), taskDescription.toString(), taskDate.toString())
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    id: Long,
    title: String,
    description: String,
    date: String,
    viewModel: UpdateTaskViewModel = hiltViewModel()
) {


    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background_color))
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                (context as Activity).finish()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                            Text(
                                text = "To Do List",
                                color = Color.White,
                                fontSize = Dimen.MediumFontSize,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = colorResource(R.color.blue)),
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(R.color.background_color))
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                EditTaskScreen(
                    id = id,
                    title = title,
                    description = description,
                    date = date,
                    onSave = { id, updatedTitle, updatedDescription, updatedDate ->
                        val updatedTaskEntity =
                            TaskEntity(id, updatedTitle, updatedDescription, updatedDate.toLong())
                        viewModel.viewModelScope.launch {
                            try {
                                viewModel.channel.send(UpdateTaskIntent.updateTask(updatedTaskEntity))
                            } catch (e: Exception) {
                                // Optionally log or handle errors here
                                Toast.makeText(
                                    context,
                                    "Error Sending Update Request",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            }
        }
    }
    val updateTaskState by viewModel.state.collectAsState()

    LaunchedEffect(updateTaskState) {
        when (updateTaskState) {
            is UpdateTaskStates.updateTasks -> {
                // Handle task update confirmation (e.g., show a toast or navigate back)
                Toast.makeText(context, "Task Updated", Toast.LENGTH_LONG).show()

                // Set the result to notify of update
                (context as Activity).apply {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }

            is UpdateTaskStates.Error -> {
                // Handle error (e.g., show error message)
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()

            }

            else -> {}
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
}