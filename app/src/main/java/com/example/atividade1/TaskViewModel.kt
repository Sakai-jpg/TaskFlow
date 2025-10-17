package com.example.atividade1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TaskUiState(
    val tasks: List<Task> = emptyList()
)

class TaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private var taskCounter = 0

    init {
        loadSampleTasks()
        taskCounter = _uiState.value.tasks.size
    }

    fun addTask(title: String, priority: Priority) {
        taskCounter++
        val newTask = Task(
            id = taskCounter,
            title = title,
            priority = priority
        )
        _uiState.update { currentState ->
            currentState.copy(
                tasks = currentState.tasks + newTask
            )
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(isCompleted = !task.isCompleted)
                } else {
                    task
                }
            }
            currentState.copy(tasks = updatedTasks)
        }
    }

    private fun loadSampleTasks() {
        val sampleTasks = listOf(
            Task(1, "Estudar Jetpack Compose", Priority.ALTA),
            Task(2, "Fazer compras", Priority.MÉDIA),
            Task(3, "Passear com o cachorro", Priority.BAIXA, isCompleted = true),
            Task(4, "Preparar a apresentação", Priority.ALTA),
            Task(5, "Ler um capítulo do livro", Priority.MÉDIA)
        )
        _uiState.value = TaskUiState(tasks = sampleTasks)
    }
}