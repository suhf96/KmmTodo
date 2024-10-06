package com.gyadam.kmmtodo.data

import com.gyadam.kmmtodo.domain.RequestState
import com.gyadam.kmmtodo.domain.ToDoTask
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoDB {
    private var realm: Realm? = null

    init {
        configureRealm()
    }

    private fun configureRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(ToDoTask::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun readAllTasks(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>(query = "isDone == $0", false)
            ?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list.sortedByDescending { toDoTask -> toDoTask.favourite })
            } ?: flow { RequestState.Error(message = "Realm is not available") }
    }

    fun readCompletedTask(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>(query = "isDone == $0", true)
            ?.asFlow()
            ?.map { result ->
                RequestState.Success(result.list)
            } ?: flow { RequestState.Error(message = "Realm is not available") }
    }

    suspend fun addTask(task: ToDoTask) {
        realm?.write { copyToRealm(task) }
    }

    suspend fun updateTask(task: ToDoTask) {
        realm?.write {
            try {
                val queriedTask = query<ToDoTask>("_id == $0", task._id).first().find()
                queriedTask?.let {
                    findLatest(it)?.let { currentTask ->
                        currentTask.title = task.title
                        currentTask.description = task.description
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    suspend fun setCompleted(task: ToDoTask, taskCompleted: Boolean) {
        realm?.write {
            try {
                val queriedTask = query<ToDoTask>("_id == $0", task._id).first().find()
                queriedTask?.apply { isDone = taskCompleted }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    suspend fun setFavourite(task: ToDoTask, isFavourite: Boolean) {
        realm?.write {
            try {
                val queriedTask = query<ToDoTask>("_id == $0", task._id).first().find()
                queriedTask?.apply { favourite = isFavourite }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    suspend fun deleteTask(task: ToDoTask) {
        realm?.write {
            try {
                val queriedTask = query<ToDoTask>("_id == $0", task._id).first().find()
                queriedTask?.let {
                    findLatest(it)?.let { currentTask ->
                        delete(currentTask)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}