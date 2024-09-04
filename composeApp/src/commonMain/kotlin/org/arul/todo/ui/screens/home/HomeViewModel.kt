package org.arul.todo.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.arul.todo.data.MongoDB
import org.arul.todo.data.RequestState
import org.arul.todo.data.models.TaskAction
import org.arul.todo.data.models.TodoTask


typealias MutableTasks = MutableState<RequestState<List<TodoTask>>>
typealias Tasks = MutableState<RequestState<List<TodoTask>>>


class HomeViewModel(private val mangoDB: MongoDB):ScreenModel {

    private var _activeTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val activeTasks: Tasks = _activeTasks

    private var _completedTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    var completedTasks: Tasks = _completedTasks

    init {
        _activeTasks.value = RequestState.Loading
        _completedTasks.value = RequestState.Loading
        screenModelScope.launch(Dispatchers.Main){
            delay(500)
            mangoDB.readActiveTasks().collectLatest {
                _activeTasks.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mangoDB.readCompletedTasks().collectLatest {
                _completedTasks.value = it
            }
        }
    }

    fun setAction(action: TaskAction){

        when(action){
            is TaskAction.SetCompleted -> {
                setCompleted(action.task,action.completed)
            }
            is TaskAction.SetFavorite ->{
                setFavorite(action.task,action.favorite)
            }
            is TaskAction.Delete -> {
                delete(action.task)
            }

            else -> {}
        }
    }
    private fun setCompleted(task: TodoTask, completed: Boolean){
        screenModelScope.launch(Dispatchers.IO) {
            mangoDB.setCompletedTask(task,completed)
        }
    }
    private fun setFavorite(task: TodoTask, isFavorite: Boolean){
        screenModelScope.launch(Dispatchers.IO) {
            mangoDB.setFavoriteTask(task,isFavorite)
        }
    }
    private fun delete(task: TodoTask){
        screenModelScope.launch(Dispatchers.IO) {
            mangoDB.deleteTask(task)
        }
    }
}