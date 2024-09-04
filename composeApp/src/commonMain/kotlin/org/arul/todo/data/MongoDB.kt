package org.arul.todo.data

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.arul.todo.data.models.TodoTask

class MongoDB {

    private var realm: Realm? = null

    init {
        realmConfiguration()
    }
    private fun realmConfiguration(){
        if(realm==null || realm!!.isClosed()){
            val config = RealmConfiguration.Builder(
                schema = setOf(TodoTask::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun readActiveTasks(): Flow<RequestState<List<TodoTask>>>{

        return realm?.query<TodoTask>(query = "completed==$0", false)?.asFlow()?.
                map { result ->
                    RequestState.Success(
                        data = result.list.sortedByDescending { task -> task.fav }
                    )
                }?: flow { RequestState.Error(message = "Realm is not available") }
    }

    fun readCompletedTasks(): Flow<RequestState<List<TodoTask>>>{

        return realm?.query<TodoTask>(query = "completed==$0", true)?.asFlow()?.
        map { result ->
            RequestState.Success(data = result.list)
        }?: flow { RequestState.Error(message = "Realm is not available") }
    }

    suspend fun addTask(task: TodoTask){
        realm?.write { copyToRealm(task) }
    }

    suspend fun updateTask(task: TodoTask){
        realm?.write {

            try {
                val queriedTask = query<TodoTask>("_id == $0",task._id)
                    .first()
                    .find()
                queriedTask?.let {
                    findLatest(it)?.let {
                        it.title = task.title
                        it.desc = task.desc
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    suspend fun deleteTask(task: TodoTask){
        realm?.write {

            try {
                val queriedTask = query<TodoTask>("_id == $0",task._id)
                    .first()
                    .find()
                queriedTask?.let {
                    findLatest(it)?.let {
                       delete(it)
                    }
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }
    suspend fun setCompletedTask(task: TodoTask, taskCompleted: Boolean){
        realm?.write {

            try {
                val queriedTask = query<TodoTask>("_id == $0",task._id)
                    .first()
                    .find()
                queriedTask?.apply {
                    completed = taskCompleted
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }

    suspend fun setFavoriteTask(task: TodoTask, isFavorite: Boolean){
        realm?.write {

            try {
                val queriedTask = query<TodoTask>("_id == $0",task._id)
                    .first()
                    .find()
                queriedTask?.apply {
                    fav = isFavorite
                }
            }catch (e: Exception){
                println(e)
            }
        }
    }
}