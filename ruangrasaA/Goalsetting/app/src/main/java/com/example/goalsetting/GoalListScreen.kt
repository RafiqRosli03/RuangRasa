package com.example.goalsetting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class Goal(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val deadline: Date?,
    val category: String,
    var completed: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalListScreen(
    goals: List<Goal>,
    onAddGoalClick: () -> Unit,
    onDeleteGoal: (Goal) -> Unit,
    onToggleComplete: (Goal) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Goals", color = Color.White) },
                actions = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C2C2E)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddGoalClick,
                containerColor = Color(0xFF6C63FF)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Goal",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2C2C2E))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(goals) { goal ->
                GoalCard(
                    goal = goal,
                    onDeleteClick = { onDeleteGoal(goal) },
                    onToggleComplete = { onToggleComplete(goal) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(
    goal: Goal,
    onDeleteClick: () -> Unit,
    onToggleComplete: (Goal) -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDeadline = goal.deadline?.let { dateFormat.format(it) }
    val isButtonClicked = remember { mutableStateOf(goal.completed) }
    val buttonColor = if (isButtonClicked.value) Color(0xFF4CAF50) else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = goal.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            isButtonClicked.value = !isButtonClicked.value
                            onToggleComplete(goal)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = Color.White
                        )
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isButtonClicked.value) "Completed" else "Mark as Done"
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }

            formattedDeadline?.let {
                Text(
                    text = "Deadline: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (isButtonClicked.value) {
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}