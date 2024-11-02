package com.example.todoapp.edittask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoapp.Dimen
import com.example.todoapp.R
import com.example.todoapp.addtaskbottomsheet.DatePickerModal
import com.example.todoapp.addtaskbottomsheet.convertMillisToDate
import com.example.todoapp.homescreen.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    id: Long,
    title: String,
    description: String,
    date: String?,
    modifier: Modifier = Modifier,
    onSave: (Long, String, String, Long) -> Unit
) {

    var taskTitle by remember { mutableStateOf(title) }
    var taskDescription by remember { mutableStateOf(description) }
    var taskDate by remember { mutableStateOf(date ?: "") }


    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }


    Column(

        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .padding(
                top = Dimen.ExtraLargePadding,
                start = Dimen.ExtraLargePadding,
                end = Dimen.ExtraLargePadding,
                bottom = Dimen.ExtraLargePadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(Dimen.MediumRoundShape),
            elevation = CardDefaults.elevatedCardElevation(Dimen.LargeElevation),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit text",
                    fontSize = Dimen.MediumFontSize,
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(Dimen.MediumPadding)
                )


                // Title field with error handling
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { newTitle ->
                        taskTitle = newTitle
//                    isTitleError = taskTitle.isEmpty()
                    },
                    label = { Text(text = "Task title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimen.MediumPadding),
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

                // Description field with error handling
                OutlinedTextField(
                    value = taskDescription!!.toString(),
                    onValueChange = { newDescription ->
                        taskDescription = newDescription
//                    isDescriptionError = taskDescription.isEmpty()
                    },
                    label = { Text(text = "Task description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimen.MediumPadding),
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


                OutlinedTextField(
                    value = taskDate?.toLongOrNull()?.let { formatDate(it) } ?: formatDate(
                        selectedDate
                    ).toString(),
                    onValueChange = {

                    },
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
                        IconButton(
                            onClick = { showDatePicker = true }) {
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
                        onSave(id, taskTitle, taskDescription, selectedDate)

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.blue)),
                    modifier = Modifier.padding(top = Dimen.LargePadding)
                ) {
                    Text(
                        text = "Save Change",
                        color = Color.White,
                        modifier = Modifier.padding(Dimen.SmallPadding)
                    )

                }
            }
        }
    }
}


interface TaskUpdateCallback {
    fun onTaskUpdated(id: Long, title: String, description: String, date: String)
}
