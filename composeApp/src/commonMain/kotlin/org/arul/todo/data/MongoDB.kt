package org.arul.todo.data

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.arul.todo.data.models.TodoTask

class MongoDB {

    var realm: Realm? = null

    fun realmConfiguration(){
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
}