package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle system UI insets properly (status bar/navigation bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: TaskViewModel by viewModels()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onAddTaskClick = { navController.navigate("add_task") },
                            onEditTaskClick = { taskId ->
                                navController.navigate("add_task/$taskId")
                            }
                        )
                    }

                    composable("add_task") {
                        AddTaskScreen(
                            onSave = { title, description ->
                                viewModel.addTask(title, description)
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("add_task/{taskId}") { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId")?.toLong()
                        val task = taskId?.let { viewModel.getTaskById(it) }

                        AddTaskScreen(
                            task = task,
                            onSave = { title, description ->
                                if (taskId != null) {
                                    viewModel.updateTask(taskId, title, description)
                                }
                                navController.popBackStack()
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Long) -> Unit

) {
    val tasks by viewModel.tasks.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text("Task Manager") },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        TaskList(
            tasks = tasks,
            onToggleCompleted = { viewModel.toggleCompleted(it) },
            onDelete = { viewModel.deleteTask(it) },
            onEdit = { onEditTaskClick(it) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}