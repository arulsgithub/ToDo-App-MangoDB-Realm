package org.arul.todo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.arul.todo.data.models.TodoTask

@Composable
fun TaskView(
    task: TodoTask,
    showActive: Boolean = true,
    onSelectedTask: (TodoTask) -> Unit,
    onFavoriteTask: ((TodoTask, Boolean) -> Unit),
    onCompleteTask: ((TodoTask, Boolean) -> Unit),
    onDeleteTask: (TodoTask) -> Unit
){

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if(showActive) onSelectedTask(task) else onDeleteTask(task)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            Checkbox(
                checked = task.completed,
                onCheckedChange = {
                    onCompleteTask(task, !task.completed)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.title,
                modifier = Modifier.alpha(if(showActive) 1f else 0.5f),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textDecoration = if(showActive) TextDecoration.None else TextDecoration.LineThrough
            )
        }
        IconButton(
            onClick = { if(showActive) onFavoriteTask(task,!task.fav) else onDeleteTask(task) }
        ){
            Icon(
                imageVector = if(showActive) Icons.Default.Star else Icons.Default.Delete,
                contentDescription = null,
                tint = if(task.fav) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }
}