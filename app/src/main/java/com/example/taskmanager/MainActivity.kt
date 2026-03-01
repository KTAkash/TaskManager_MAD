package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Allows drawing behind system bars (edge-to-edge UI)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme {

                val navController = rememberNavController()

                // ViewModel scoped to this Activity lifecycle
                val viewModel: TaskViewModel by viewModels()

                // Navigation graph configuration
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {

                    // Home screen route
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onAddTaskClick = {
                                navController.navigate("add_task")
                            },
                            onEditTaskClick = { taskId ->
                                navController.navigate("add_task/$taskId")
                            }
                        )
                    }

                    // Add Task route
                    composable("add_task") {
                        AddTaskScreen(
                            onSave = { title, description ->
                                viewModel.addTask(title, description)
                                navController.popBackStack() // Return to Home
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // Edit Task route with argument
                    composable("add_task/{taskId}") { backStackEntry ->

                        // Retrieve taskId from navigation arguments
                        val taskId =
                            backStackEntry.arguments?.getString("taskId")?.toLong()

                        // Fetch task from ViewModel
                        val task = taskId?.let {
                            viewModel.getTaskById(it)
                        }

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

    // Observe LiveData as Compose state for automatic recomposition
    val tasks by viewModel.tasks.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Manager") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },

        // Floating Action Button to add new tasks
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task", // Accessibility
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->

        // Task list content
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