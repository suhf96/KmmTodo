package com.gyadam.kmmtodo.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.gyadam.kmmtodo.data.MongoDB
import com.gyadam.kmmtodo.domain.RequestState
import com.gyadam.kmmtodo.domain.ToDoTask
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal typealias MutableTasks = MutableState<RequestState<List<ToDoTask>>>
internal typealias Tasks = MutableState<RequestState<List<ToDoTask>>>

class HomeScreenViewModel(private val mongodb: MongoDB) : ScreenModel {

    private var _activeTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val activeTasks: Tasks = _activeTasks

    private var _completedTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val completedTasks: Tasks = _completedTasks

    init {
        _activeTasks.value = RequestState.Loading
        _completedTasks.value = RequestState.Loading

        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mongodb.readAllTasks().collectLatest {
                _activeTasks.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mongodb.readCompletedTask().collectLatest {
                _completedTasks.value = it
            }
        }
    }
}