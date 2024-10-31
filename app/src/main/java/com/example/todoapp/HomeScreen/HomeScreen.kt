package com.example.todoapp.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.todoapp.Dimen
import com.example.todoapp.R


@Composable
fun HomeScreen() {

}


@Composable
fun TaskItem() {

    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(.2f)
            .padding(Dimen.MediumPadding)
            .background(color = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            VerticalLineWithCorners()

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Play basketBall",
                    fontSize = Dimen.MediumFontSize,
                    color = colorResource(R.color.blue),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.blue))
            ) {
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier.padding(
                        horizontal = 4.dp,
                        vertical = 2.dp
                    ),
                    colors = IconButtonColors(
                        containerColor = colorResource(R.color.blue),
                        contentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    ),

                    ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = Color.White
                    )
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
            .padding(
                top = Dimen.MediumElevation,
                bottom = Dimen.MediumOffSet,
                start = Dimen.LargePadding,
                end = Dimen.LargePadding
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(color)

    )
}