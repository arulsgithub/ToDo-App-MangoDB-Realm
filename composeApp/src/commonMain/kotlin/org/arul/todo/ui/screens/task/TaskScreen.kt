package org.arul.todo.ui.screens.task

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.arul.todo.data.models.TaskAction
import org.arul.todo.data.models.TodoTask


const val DEFAULT_TITLE = "Enter the title"
const val DEFAULT_DESCRIPTION = "Add the description"
class TaskScreen( val task: TodoTask? = null): Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel = getScreenModel<TaskViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var currentTitle by remember { mutableStateOf(task?.title ?:DEFAULT_TITLE) }
        var currentDescription by remember { mutableStateOf(task?.desc ?:DEFAULT_DESCRIPTION) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        BasicTextField(
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            ),
                            singleLine = true,
                            value = currentTitle,
                            onValueChange = {currentTitle=it}
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop()}){
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if(currentTitle.isNotEmpty() && currentDescription.isNotEmpty()){
                    FloatingActionButton(
                        onClick = {
                            if(task!=null){
                                viewModel.setAction(
                                    action = TaskAction.Update(
                                        TodoTask().apply {
                                            title = currentTitle
                                            desc = currentDescription
                                        }
                                    )
                                )
                            }else{
                                viewModel.setAction(
                                    action = TaskAction.Add(
                                        TodoTask().apply {
                                            title = currentTitle
                                            desc = currentDescription
                                        }
                                    )
                                )
                            }
                            navigator.pop()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ){
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checkmark Icon"
                        )
                    }
                }
            }
        ) {padding ->
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .padding(all = 26.dp)
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    ),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                value = currentDescription,
                onValueChange = {currentDescription=it}
            )

        }
    }
}