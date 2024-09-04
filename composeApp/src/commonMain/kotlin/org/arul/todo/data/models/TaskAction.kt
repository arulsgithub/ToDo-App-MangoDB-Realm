package org.arul.todo.data.models

sealed class TaskAction {

    data class Add(val task: TodoTask): TaskAction()
    data class Update(val task: TodoTask): TaskAction()
    data class Delete(val task: TodoTask): TaskAction()
    data class SetCompleted(val task: TodoTask, val completed: Boolean): TaskAction()
    data class SetFavorite(val task: TodoTask, val favorite: Boolean): TaskAction()


}