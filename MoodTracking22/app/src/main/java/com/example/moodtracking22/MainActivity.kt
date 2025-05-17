@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.moodtracking22

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Tracking", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            )
        }
    ) {
        MoodTrackingContent()
    }
}

@Composable
fun MoodTrackingContent() {
    val selectedDate = remember { mutableStateOf("") }
    val moodRatings = remember { mutableStateMapOf<String, Pair<Float, Float>>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Adjusts spacing to fit button at the bottom
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RateSection(
                onSave = { sadness ->
                    if (selectedDate.value.isNotEmpty()) {
                        moodRatings[selectedDate.value] = sadness to sadness
                    }
                },
                onRemove = {
                    if (selectedDate.value.isNotEmpty()) {
                        moodRatings.remove(selectedDate.value)
                    }
                }
            )
            Divider(
                color = Color.Gray.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            JourneySection(selectedDate, moodRatings)
        }

        // Button to navigate to MoodAnalysisActivity
        val context = LocalContext.current // Move this inside the Composable

        Button(
            onClick = {
                val intent = Intent(context, MoodAnalysisActivity::class.java)
                intent.putExtra("moodRatings", HashMap(moodRatings))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFAB91))
        ) {
            Text("Go to Mood Analysis", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun RateSection(onSave: (Float) -> Unit, onRemove: () -> Unit) {
    var sadness by remember { mutableStateOf(1f) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Rate Your Mood", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        RatingSlider(label = "Rate Your Mood", color = Color(0xFFFFAB91), value = sadness) {
            sadness = it
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { onSave(sadness) }) {
                Text("Save Mood")
            }
            Button(onClick = onRemove, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Remove Mood", color = Color.White)
            }
        }
    }
}

@Composable
fun RatingSlider(label: String, color: Color, value: Float, onValueChange: (Float) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Rate Your", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..3f,
            steps = 1,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                inactiveTrackColor = color.copy(alpha = 0.3f)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Slightly", fontSize = 14.sp)
            Text(text = "Moderate", fontSize = 14.sp)
            Text(text = "Very", fontSize = 14.sp)
        }
    }
}

@Composable
fun JourneySection(
    selectedDate: MutableState<String>,
    moodRatings: MutableMap<String, Pair<Float, Float>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F1F1), shape = CircleShape)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Journey", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        CalendarView(selectedDate, moodRatings)

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedDate.value.isNotEmpty()) {
            val mood = moodRatings[selectedDate.value]
            if (mood != null) {
                val (sadness, _) = mood
                Text(
                    text = "Mood on ${selectedDate.value}: Mood - $sadness",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "No mood recorded for ${selectedDate.value}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CalendarView(
    selectedDate: MutableState<String>,
    moodRatings: MutableMap<String, Pair<Float, Float>>
) {
    Column {
        val days = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

        // Weekdays Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            days.forEach { day ->
                Text(text = day, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Example Calendar Layout
        val calendarDays = (1..31).toList()
        calendarDays.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                week.forEach { day ->
                    val date = "2025-01-$day" // Example date format
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = moodRatings[date]?.let { Color(0xFFFFAB91) } ?: Color.White,
                                shape = CircleShape
                            )
                            .clickable { selectedDate.value = date },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            color = moodRatings[date]?.let { Color.White } ?: Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}