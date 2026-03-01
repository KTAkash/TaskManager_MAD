package com.example.taskmanager

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TaskRepository(application)

    private val _tasks = MutableLiveData<List<Task>>() // Backing LiveData
    val tasks: LiveData<List<Task>> = _tasks // Exposed immutable LiveData

    init {
        loadTasks() // Load tasks when ViewModel is created
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return // Prevent adding tasks with empty title
        val newTask = Task(title = title.trim(), description = description.trim())
        val currentList = _tasks.value ?: emptyList()
        _tasks.value = listOf(newTask) + currentList // Add new task to top of list
        saveTasks() // Persist changes
    }

    fun getTaskById(taskId: Long): Task? {
        return _tasks.value?.find { it.id == taskId } // Find task by ID
    }

    fun deleteTask(taskId: Long) {
        val updatedList = (_tasks.value ?: emptyList()).filter { it.id != taskId } // Remove task
        _tasks.value = updatedList
        saveTasks() // Persist changes
    }

    fun updateTask(taskId: Long, title: String, description: String) {
        if (title.isBlank()) return // Prevent empty title update

        val updatedList = (_tasks.value ?: emptyList()).map {
            if (it.id == taskId) {
                it.copy(
                    title = title.trim(),
                    description = description.trim() // Update title & description
                )
            } else it
        }

        _tasks.value = updatedList
        saveTasks() // Persist changes
    }

    fun toggleCompleted(taskId: Long) {
        val updatedList = (_tasks.value ?: emptyList()).map {
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it // Toggle completed flag
        }
        _tasks.value = updatedList
        saveTasks() // Persist changes
    }

    private fun loadTasks() {
        _tasks.value = repository.getTasks() // Load tasks from repository
    }

    private fun saveTasks() {
        viewModelScope.launch { // Save tasks asynchronously
            repository.saveTasks(_tasks.value ?: emptyList())
        }
    }
}