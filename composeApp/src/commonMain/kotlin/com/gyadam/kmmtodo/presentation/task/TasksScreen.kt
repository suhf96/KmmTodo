package com.gyadam.kmmtodo.presentation.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gyadam.kmmtodo.domain.TaskAction
import com.gyadam.kmmtodo.domain.ToDoTask

const val DEFAULT_TASK = "Enter the task name"
const val DEFAULT_TASK_DESCP = "Enter the task description"

class TasksScreen(val task: ToDoTask? = null) : Screen {


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<TaskViewModel>()

        var currentTitle by remember {
            mutableStateOf(task?.title ?: DEFAULT_TASK)
        }
        var currentDescription by remember {
            mutableStateOf(task?.description ?: DEFAULT_TASK_DESCP)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Add Task", style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            ), textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back Arrow"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if (currentTitle.isNotEmpty() && currentDescription.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = {
                            if (task != null) {
                                viewModel.setAction(TaskAction.Update(
                                    ToDoTask().apply {
                                        _id = task._id
                                        title = currentTitle
                                        description = currentDescription
                                    }
                                ))
                            } else {
                                viewModel.setAction(TaskAction.Update(
                                    ToDoTask().apply {
                                        title = currentTitle
                                        description = currentDescription
                                    }
                                ))
                            }
                            navigator.pop()
                        },
                        shape = RoundedCornerShape(size = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checkmark Icon"
                        )
                    }
                }
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 24.dp),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    ),

                    singleLine = true,
                    value = currentTitle,
                    onValueChange = { currentTitle = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 24.dp),

                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    value = currentDescription,
                    onValueChange = { description -> currentDescription = description }
                )
            }

        }
    }
}