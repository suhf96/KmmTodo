package com.gyadam.kmmtodo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.gyadam.kmmtodo.data.MongoDB
import com.gyadam.kmmtodo.presentation.home.HomeScreen
import com.gyadam.kmmtodo.presentation.home.HomeScreenViewModel
import com.gyadam.kmmtodo.presentation.task.TaskViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.dsl.module

val lightColor = Color(color = 0xFF57D88)
val darkColor = Color(color = 0xFF57D88)

@Composable
@Preview
fun App() {
    initializeKoin()
    MaterialTheme {
        Navigator(HomeScreen()) {
            SlideTransition(it)
        }
    }
}

val mongoModule = module {
    single { MongoDB() }
    factory { HomeScreenViewModel(get()) }
    factory { TaskViewModel(get()) }
}

fun initializeKoin() = startKoin {
    modules(mongoModule)
}
