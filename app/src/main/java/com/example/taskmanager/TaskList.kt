package com.example.taskmanager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Edit

@Composable
fun TaskList(
    tasks: List<Task>,
    onToggleCompleted: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onEdit: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No tasks yet. Tap + to add one!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                items = tasks,
                key = { it.id }
            ) { task ->
                TaskItem(
                    task = task,
                    onToggleCompleted = { onToggleCompleted(task.id) },
                    onDelete = { onDelete(task.id) },
                    onEdit = { onEdit(task.id) }

                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleCompleted: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit

) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleCompleted() }
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit task",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (task.description.isNotEmpty()) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = if (task.isCompleted) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 32.dp, top = 4.dp)
                )
            }
        }
    }
}