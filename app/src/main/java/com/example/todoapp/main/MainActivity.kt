package com.example.todoapp.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.domain.model.Task
import com.example.todoapp.Dimen
import com.example.todoapp.homescreen.HomeScreen
import com.example.todoapp.R
import com.example.todoapp.addtaskbottomsheet.AddTaskBottomSheet
import com.example.todoapp.addtaskbottomsheet.InsertViewModel
import com.example.todoapp.edittask.EditTaskScreen
import com.example.todoapp.homescreen.HomeViewModel
import com.example.todoapp.setting.SettingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val sheetState = rememberModalBottomSheetState()
//    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden)

    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val tasks by viewModel.tasks.collectAsState()

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
                        // Show the bottom sheet
                        showBottomSheet = true
                    },
                    containerColor = colorResource(R.color.blue),
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier.offset(y = Dimen.ExtraLargeOffSet)
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
                composable("Home Screen") { HomeScreen(navController) }
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

                AddTaskBottomSheet(onDismiss = {
                    showBottomSheet = false
                }, onTaskAdded = { newTask ->
                    Log.d("AddTaskBottomSheet", "New task added: $newTask")
                })
            }
        }
    }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
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