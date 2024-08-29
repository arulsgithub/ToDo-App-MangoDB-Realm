package org.arul.todo.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.arul.todo.data.RequestState
import org.arul.todo.data.models.TodoTask
import org.arul.todo.ui.components.ErrorIndicator
import org.arul.todo.ui.components.LoadingIndicator
import org.arul.todo.ui.components.TaskView

class HomeScreen: Screen{

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text(text = "Todo") })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    shape = RoundedCornerShape(size = 10.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
            }
        ) {padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    )
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTasks(
    modifier: Modifier = Modifier,
    tasks: RequestState<List<TodoTask>>,
    showActive: Boolean = true,
    onSelectedTask: ((TodoTask) -> Unit)? = null,
    onFavoriteTask: ((TodoTask, Boolean) -> Unit)? = null,
    onCompleteTask: ((TodoTask, Boolean) -> Unit),
    onDeleteTask: ((TodoTask) -> Unit)? = null
){

    var showDialog by remember { mutableStateOf(false) }
    var deletionOfTask: TodoTask? by remember { mutableStateOf(null) }

    if(showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                deletionOfTask = null
            },
            title = {
                Text(
                    text = "Delete Task",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete ${deletionOfTask!!.title}?",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTask?.invoke(deletionOfTask!!)
                        showDialog = false
                        deletionOfTask = null
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        deletionOfTask = null
                    }
                ) {
                    Text(text = "No")
                }
            }
        )
    }
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = if(showActive) "Active Tasks" else "Completed Tasks",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        tasks.DisplayResult(
            onLoading = { LoadingIndicator() },
            onError = { ErrorIndicator(message = it) },
            onSuccess = {
                if(it.isNotEmpty()){

                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        items(
                            items = it,
                            key = { task -> task._id.toHexString() }
                        ){ task ->
                            TaskView(
                                showActive = showActive,
                                task = task,
                                onSelectedTask = { onSelectedTask?.invoke(task) },
                                onCompleteTask = { selectedTask, completed ->
                                    onCompleteTask(selectedTask, completed)
                                },
                                onFavoriteTask = { selectedTask, favorite ->
                                    onFavoriteTask?.invoke(selectedTask, favorite)
                                },
                                onDeleteTask = { selectedTask ->
                                    deletionOfTask = selectedTask
                                    showDialog = true
                                }
                            )
                        }
                    }
                }else{
                    ErrorIndicator()
                }
            }

        )
    }
}

