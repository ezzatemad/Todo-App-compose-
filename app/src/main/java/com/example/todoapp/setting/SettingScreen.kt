package com.example.todoapp.setting

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.todoapp.Dimen
import com.example.todoapp.R

@Composable
fun SettingScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.ExtraLargePadding)
            .background(Color.Transparent)
    ) {
        val languages = listOf("English", "Arabic")
        val modes = listOf("Light", "Dark")

        Text(
            text = "Language",
            style = TextStyle(fontWeight = FontWeight.ExtraLight),
            fontSize = Dimen.MediumFontSize,
            color = Color.Black,
            modifier = Modifier.padding(Dimen.LargePadding)
        )
        SpinnerSetting(options = languages)

        Text(
            text = "Mode",
            style = TextStyle(fontWeight = FontWeight.ExtraLight),
            fontSize = Dimen.MediumFontSize,
            color = Color.Black,
            modifier = Modifier.padding(Dimen.LargePadding)
        )
        SpinnerSetting(options = modes)

    }


}



@Composable
fun SpinnerSetting(options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Choose an option") }

    // This handles the animation of the dropdown position.
    val transition = remember { androidx.compose.animation.core.Animatable(0f) }

    // Use LaunchedEffect to react to changes in the 'expanded' state
    LaunchedEffect(expanded) {
        val targetValue = if (expanded) 1f else 0f
        // Animate the transition whenever 'expanded' state changes.
        transition.animateTo(targetValue)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(Dimen.LargePadding),
    ) {
        Text(
            text = selectedOption,
            color = colorResource(R.color.blue),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, color = colorResource(R.color.blue))
                .clickable {
                    expanded = !expanded
                }
                .padding(16.dp)
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimen.LargePadding, end = Dimen.LargePadding)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        expanded = false
                    },
                    text = { Text(text = option, color = colorResource(R.color.blue)) }
                )
            }
        }

        // Icon for toggling the dropdown
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { expanded = !expanded }
                .padding(end = 16.dp),
            tint = colorResource(R.color.blue)
        )
    }
}


