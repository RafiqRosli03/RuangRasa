package com.example.goalsetting.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goalsetting.ui.theme.GoalSettingTheme
import java.text.SimpleDateFormat
import java.util.*

sealed class Screen(val route: String) {
    object GoalList : Screen("goalList")
    object CreateGoal : Screen("createGoal")
}

@Composable
fun GoalSettingApp() {
    val navController = rememberNavController()
    val goals = remember { mutableStateListOf<Goal>() }

    NavHost(
        navController = navController,
        startDestination = Screen.GoalList.route
    ) {
        composable(Screen.GoalList.route) {
            GoalListScreen(
                goals = goals,
                onAddGoalClick = { navController.navigate(Screen.CreateGoal.route) },
                onDeleteGoal = { goalToDelete -> goals.remove(goalToDelete) },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(Screen.CreateGoal.route) {
            GoalSettingScreen(
                onGoalCreated = { newGoal ->
                    goals.add(newGoal)
                    navController.navigateUp()
                },
                navController = navController
            )
        }
    }
}

@Composable
fun GoalSettingScreen(
    onGoalCreated: (Goal) -> Unit,
    navController: NavController
) {
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var titleText by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf("") }
    val context = LocalContext.current

    GoalSettingTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0D47A1)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                TopBar(
                    title = "Create New Goal",
                    onBackClick = { navController.navigateUp() } // Added the missing parameter
                )

                Spacer(modifier = Modifier.height(16.dp))

                DateSelector(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                GoalInputCard(
                    title = titleText,
                    onTitleChange = { titleText = it },
                    category = categoryText,
                    onCategoryChange = { categoryText = it },
                    onSubmit = {
                        if (titleText.isNotBlank() && selectedDate != null) {
                            val newGoal = Goal(
                                title = titleText,
                                deadline = selectedDate,
                                category = categoryText.ifBlank { "Uncategorized" }
                            )
                            onGoalCreated(newGoal)
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in all required fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalInputCard(
    title: String,
    onTitleChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Goal Title") },
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = category,
                onValueChange = onCategoryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Write your description...") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSubmit,
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D47A1)
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("Create Goal")
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }


    }
}

@Composable
private fun DateSelector(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF64B5F6)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Deadline",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedDate?.let { dateFormat.format(it) } ?: "Select a date",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            IconButton(
                onClick = {
                    val currentDate = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            calendar.set(year, month, day)
                            onDateSelected(calendar.time)
                        },
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = currentDate.timeInMillis
                    }.show()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date"
                )
            }
        }
    }
}
