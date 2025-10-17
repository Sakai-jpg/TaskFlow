package com.example.atividade1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.atividade1.ui.theme.atividade1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            atividade1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskScreen()
                }
            }
        }
    }
}

@Composable
fun TaskScreen(taskViewModel: TaskViewModel = viewModel()) {
    val uiState by taskViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "TaskFlow",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.tasks) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = { taskViewModel.toggleTaskCompletion(task.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AddTaskSection(
            onAddTask = { title, priority ->
                taskViewModel.addTask(title, priority)
            }
        )
    }
}

@Composable
fun TaskItem(task: Task, onCheckedChange: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Prioridade: ${task.priority.name}",
                    fontSize = 14.sp,
                    color = when (task.priority) {
                        Priority.ALTA -> Color.Red
                        Priority.MÉDIA -> Color(0xFFFFA500)
                        Priority.BAIXA -> Color.Gray
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskSection(onAddTask: (String, Priority) -> Unit) {
    var taskTitle by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MÉDIA) }

    Column {
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            label = { Text("Nome da Tarefa") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Prioridade:", style = MaterialTheme.typography.labelMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Priority.values().forEach { priority ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (priority == selectedPriority),
                        onClick = { selectedPriority = priority }
                    )
                    Text(text = priority.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (taskTitle.isNotBlank()) {
                    onAddTask(taskTitle, selectedPriority)
                    taskTitle = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Tarefa")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    atividade1Theme {
        TaskScreen()
    }
}