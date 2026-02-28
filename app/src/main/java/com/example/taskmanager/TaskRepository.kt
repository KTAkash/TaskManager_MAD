package com.example.taskmanager

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TaskRepository(context: Context) {

 /* Using MODE_PRIVATE ensures that the SharedPreferences file
    is accessible only to this application.
    This prevents other apps from reading or modifying stored task data.*/
    private val sharedPreferences = context.getSharedPreferences("tasks_prefs", Context.MODE_PRIVATE)

    fun saveTasks(tasks: List<Task>) {
      /* Data is serialized into JSON before storing.
         Structured serialization reduces the risk of improper data formatting
         and helps maintain data integrity when saving complex objects. */
        val json = Gson().toJson(tasks)
        sharedPreferences.edit().putString("tasks", json).apply()
    }

    fun getTasks(): List<Task> {
        /* Providing a default value ("[]") prevents null-related crashes
        and ensures the app behaves safely even if no data exists.*/
        val json = sharedPreferences.getString("tasks", "[]") ?: "[]"
        return try {
            /* TypeToken ensures correct type deserialization,
             reducing risks of type mismatch or runtime errors.*/
            val type: Type = object : TypeToken<List<Task>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            /* Exception handling prevents the app from crashing
             if stored data becomes corrupted or tampered with.
             Returning an empty list ensures safe fallback behavior.*/
            emptyList()
        }
    }
}