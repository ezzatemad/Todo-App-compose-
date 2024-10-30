package com.example.todoapp.addtaskbottomsheet

import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapp.Dimen
import com.example.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(onClick: () -> Unit) {

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Task",
            color = Color.Black,
            fontSize = Dimen.MediumFontSize,
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )

        OutlinedTextField(
            value = taskTitle,
            onValueChange = { newtitle ->
                taskTitle = newtitle
            },
            label = { Text(text = "task title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.MediumPadding),
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
        )

        OutlinedTextField(
            value = taskDescription,
            onValueChange = { newDescription ->
                taskDescription = newDescription
            },
            label = { Text(text = "task description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimen.MediumPadding),
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
        )

        Button(
            onClick = onClick,
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