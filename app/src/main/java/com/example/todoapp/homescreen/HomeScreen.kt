package com.example.todoapp.homescreen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.data.database.model.TaskEntity
import com.example.domain.model.Task
import com.example.todoapp.Dimen
import com.example.todoapp.R
import com.example.todoapp.edittask.EditTaskActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.channel.trySend(HomeIntent.LoadTasks)
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.SmallPadding)
    ) {

        when (state) {
            is HomeStates.Loading -> {
                item {
                    // Show a loading indicator
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

            }

            is HomeStates.Idle -> {
                // Show empty state or a message
                item {
                    Text(
                        text = "No tasks available",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }

            is HomeStates.getAllTasks -> {
                val tasks = (state as HomeStates.getAllTasks).tasksList

                items(tasks) { task ->
                    TaskItem(task = task) {
                        val intent = Intent(context, EditTaskActivity::class.java).apply {
                            putExtra("TASK_ID", task.id)
                            putExtra("TASK_TITLE", task.title)
                            putExtra("TASK_DESCRIPTION", task.description)
                            putExtra("TASK_DATE", task.date)
                        }
                        context.startActivity(intent)
                    }
                }
                Log.d("TAG", "HomeScreen: $tasks")
            }

            is HomeStates.Error -> {
                item {
                    Text(
                        text = "Error loading tasks",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Red
                    )
                }
            }

            is HomeStates.TaskIsDone -> {
                Log.d("TAG", "HomeScreen: task is done")
            }
        }
    }
}


@Composable
fun TaskItem(task: TaskEntity, viewModel: HomeViewModel = hiltViewModel(), onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(Dimen.MediumPadding),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .height(IntrinsicSize.Max)
                .padding(Dimen.LargePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalLineWithCorners()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Dimen.MediumPadding)
            ) {
                Text(
                    text = task.title ?: "No Name",
                    fontSize = Dimen.MediumFontSize,
                    color = if (task.isDone == true) colorResource(R.color.task_done) else colorResource(
                        R.color.blue
                    ),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(Dimen.MediumPadding)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(Dimen.MediumPadding)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock),
                        contentDescription = null,
                        tint = Color.Black
                    )

                    Text(
                        text = formatDate(task.date!!),
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            // Change the icon button based on the task's completion status

            if (task.isDone == true) {

                Text(
                    text = "Done!",
                    fontSize = Dimen.MediumFontSize,
                    color = colorResource(R.color.task_done),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(55.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(R.color.blue)),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        onClick = {
                            // Convert the task entity to the domain model if needed
                            val updatedTask = task.copy(isDone = true)
                            viewModel.viewModelScope.launch {
                                viewModel.channel.send(HomeIntent.MarkTaskAsDone(updatedTask))
                            }
                        },
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = Color.White,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun VerticalLineWithCorners(
    color: Color = colorResource(R.color.blue),
    thickness: Dp = 6.dp,
    cornerRadius: Dp = 4.dp
) {

    Box(
        modifier = Modifier
            .width(thickness)
            .fillMaxHeight()

            .clip(RoundedCornerShape(cornerRadius))
            .background(color)

    )
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return format.format(date)
}