package com.example.todoapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.Dimen
import com.example.todoapp.HomeScreen.HomeScreen
import com.example.todoapp.HomeScreen.TaskItem
import com.example.todoapp.R
import com.example.todoapp.addtaskbottomsheet.AddTaskBottomSheet
import com.example.todoapp.setting.SettingScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            MainScreen()

            TaskItem()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background_color))
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "To Do List",
                            color = Color.White,
                            fontSize = Dimen.MediumFontSize,
                            style = TextStyle(fontStyle = FontStyle.Normal)
                        )
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = colorResource(R.color.blue)),
                )
            },
            bottomBar = { BottomNavigationBar(navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    containerColor = colorResource(R.color.blue),
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier.offset(y = Dimen.LargeOffSet)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = Color.Transparent

        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = "Home Screen",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("Home Screen") { HomeScreen() }
                composable("Setting Screen") { SettingScreen() }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.padding(bottom = 56.dp)
            ) {
                //bottom sheet content

                AddTaskBottomSheet {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, "Home Screen"),
        BottomNavItem("Settings", Icons.Filled.Settings, "Setting Screen")
    )

    BottomAppBar(
        modifier = Modifier.background(color = Color.White),
        contentColor = colorResource(R.color.blue),
        containerColor = Color.White
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEachIndexed { index, item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) colorResource(R.color.blue) else Color.Gray
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (isSelected) colorResource(R.color.blue) else Color.Gray
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}