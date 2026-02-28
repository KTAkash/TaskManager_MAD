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

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    init {
        loadTasks()
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) return // Input validation
        val newTask = Task(title = title.trim(), description = description.trim())
        val currentList = _tasks.value ?: emptyList()
        _tasks.value = listOf(newTask) + currentList
        saveTasks()
    }

    fun getTaskById(taskId: Long): Task? {
        return _tasks.value?.find { it.id == taskId }
    }

    fun deleteTask(taskId: Long) {
        val updatedList = (_tasks.value ?: emptyList()).filter { it.id != taskId }
        _tasks.value = updatedList
        saveTasks()
    }

    fun updateTask(taskId: Long, title: String, description: String) {
        if (title.isBlank()) return

        val updatedList = (_tasks.value ?: emptyList()).map {
            if (it.id == taskId) {
                it.copy(
                    title = title.trim(),
                    description = description.trim()
                )
            } else it
        }

        _tasks.value = updatedList
        saveTasks()
    }

    fun toggleCompleted(taskId: Long) {
        val updatedList = (_tasks.value ?: emptyList()).map {
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
        }
        _tasks.value = updatedList
        saveTasks()
    }

    private fun loadTasks() {
        _tasks.value = repository.getTasks()
    }

    private fun saveTasks() {
        viewModelScope.launch {
            repository.saveTasks(_tasks.value ?: emptyList())
        }
    }
}