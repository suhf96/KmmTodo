package com.gyadam.kmmtodo.presentation.task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gyadam.kmmtodo.data.MongoDB
import com.gyadam.kmmtodo.domain.TaskAction
import com.gyadam.kmmtodo.domain.ToDoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val mongodb: MongoDB) : ScreenModel {


    fun setAction(action: TaskAction){
        when(action){
            is TaskAction.Add -> addTask(action.task)
            is TaskAction.Delete -> TODO()
            is TaskAction.SetCompleted -> TODO()
            is TaskAction.SetFavorite -> TODO()
            is TaskAction.Update -> updateTask(action.task)
        }
    }
    private fun addTask(task: ToDoTask) {
        screenModelScope.launch(Dispatchers.Main) {
            mongodb.addTask(task)
        }
    }

    private fun updateTask(task: ToDoTask) {
        screenModelScope.launch(Dispatchers.Main) {
            mongodb.updateTask(task)
        }
    }
}