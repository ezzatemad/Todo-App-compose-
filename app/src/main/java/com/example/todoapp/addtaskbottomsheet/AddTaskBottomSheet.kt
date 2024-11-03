@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.todoapp.addtaskbottomsheet

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Task
import com.example.todoapp.Dimen
import com.example.todoapp.homescreen.HomeViewModel
import com.example.todoapp.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    viewModel: InsertViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onTaskAdded: (Task) -> Unit
) {

    val state by viewModel.state.collectAsState()

    // Observe InsertState to check for success and log the task details
    LaunchedEffect(state) {
        if (state is InsertState.Success) {
            val successState = state as InsertState.Success
            Log.d(
                "tag",
                "Inserted Task: Title=${successState.title}, Description=${successState.description}, Date=${successState.date}"
            )
            onTaskAdded(
                Task(
                    title = successState.title,
                    description = successState.description,
                    date = successState.date
                )
            )
        }
    }


    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    // Track error states
    var isTitleError by remember { mutableStateOf(false) }
    var isDescriptionError by remember { mutableStateOf(false) }


    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Task",
            color = Color.Black,
            fontSize = Dimen.MediumFontSize,
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )

        // Title field with error handling
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { newTitle ->
                taskTitle = newTitle
                isTitleError = taskTitle.isEmpty()
            },
            label = { Text(text = "Task title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.MediumPadding),
            isError = isTitleError,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray
            ),
            shape = RoundedCornerShape(4.dp),
        )
        if (isTitleError) {
            Text(
                text = "Title cannot be empty",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = Dimen.MediumPadding)
            )
        }

        // Description field with error handling
        OutlinedTextField(
            value = taskDescription,
            onValueChange = { newDescription ->
                taskDescription = newDescription
                isDescriptionError = taskDescription.isEmpty()
            },
            label = { Text(text = "Task description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.MediumPadding),
            isError = isDescriptionError,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray
            ),
            shape = RoundedCornerShape(4.dp),
        )
        if (isDescriptionError) {
            Text(
                text = "Description cannot be empty",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = Dimen.MediumPadding)
            )
        }

        OutlinedTextField(
            value = convertMillisToDate(selectedDate),
            onValueChange = { },
            label = { Text("Date") },
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                containerColor = Color.Transparent,
                errorTextColor = Color.Red,
                focusedLabelColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp),

            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.MediumPadding)
        )

        if (showDatePicker) {
            DatePickerModal(
                initialDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date ?: System.currentTimeMillis()
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }


        Button(
            onClick = {
                if (taskTitle.isNotBlank() && taskDescription.isNotBlank()) {
                    viewModel.viewModelScope.launch {
                        viewModel.channel.send(
                            InsertIntent.AddTask(
                                taskTitle,
                                taskDescription,
                                selectedDate
                            )
                        )
                        onDismiss()

                        // Move the task creation logic to a success handler
                        if (state is InsertState.Success) {
                            val successState = state as InsertState.Success
                            onTaskAdded(
                                Task(
                                    title = successState.title,
                                    description = successState.description,
                                    date = successState.date
                                )
                            )
                        }
                    }
                    taskTitle = ""
                    taskDescription = ""
                    selectedDate = System.currentTimeMillis()
                } else {
                    if (taskTitle.isBlank()) {
                        isTitleError = true
                    }
                    if (taskDescription.isBlank()) {
                        isDescriptionError = true
                    }
                }

            },
            modifier = Modifier
                .padding(top = Dimen.MediumPadding, bottom = 30.dp),
            colors = ButtonColors(
                containerColor = colorResource(R.color.blue),
                contentColor = colorResource(R.color.white),
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent
            )
        ) {
            Text(
                text = "Add",
                color = colorResource(R.color.white),
                modifier = Modifier.padding(Dimen.SmallPadding),
                fontSize = Dimen.SmallFontSize
            )
        }

    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun DatePickerModal(
    initialDate: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

