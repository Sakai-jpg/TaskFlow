package com.example.atividade1

enum class Priority {
    ALTA, MÉDIA, BAIXA
}

data class Task(
    val id: Int,
    val title: String,
    val priority: Priority,
    val isCompleted: Boolean = false
)