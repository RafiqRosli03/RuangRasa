package com.example.moodtracking22

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MoodAnalysisActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodAnalysisScreen()
        }
    }
}

@Composable
fun MoodAnalysisScreen() {
    val context = LocalContext.current
    val moodRatings = (context as? MoodAnalysisActivity)?.intent?.getSerializableExtra("moodRatings") as? HashMap<String, Pair<Float, Float>> ?: HashMap()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mood Analysis",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (moodRatings.isEmpty()) {
                Text("No mood data recorded yet.")
            } else {
                MoodDataTable(moodRatings)
                MoodGraph(moodRatings)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Done Button
            Button(
                onClick = {
                    // Navigate to another app or show error if not installed
                    val intent = Intent().apply {
                        component = ComponentName(
                            "com.example.ruangrasaa",
                            "com.example.ruangrasaa.MainActivity"
                        )
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "App not installed", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Done", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun MoodGraph(moodRatings: HashMap<String, Pair<Float, Float>>) {
    val moods = moodRatings.entries.map { it.value.first }
    val dates = moodRatings.keys.toList()
    val maxMood = moods.maxOrNull() ?: 3f // Assuming 3 is the max rating
    val barWidth = 30.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mood Graph",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // X-axis labels (at the top)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            dates.forEach { date ->
                Text(text = date, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }

        // Bar chart
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            moods.forEach { mood ->
                val height = (mood / maxMood) * 200 // Scale height based on max mood
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(height.dp)
                        .background(Color(0xFFFFAB91), shape = RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = mood.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MoodDataTable(moodRatings: HashMap<String, Pair<Float, Float>>) {
    val tableHeaderColor = colorResource(id = R.color.light_blue) // Replace with your actual color resource

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Table Header Row
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "Date      ", weight = 1f, color = tableHeaderColor)
                TableCell(text = "Mood", weight = 1f, color = tableHeaderColor)
            }

            // Data Rows using LazyColumn for efficiency
            LazyColumn {
                items(moodRatings.entries.toList()) { (date, mood) ->
                    val (sadness, _) = mood
                    val moodText = when (sadness) {
                        1.0f -> "Slightly"
                        2.0f -> "Moderate"
                        3.0f -> "Very"
                        else -> "Unknown"
                    }
                    Row(Modifier.fillMaxWidth()) {
                        TableCell(text = date, weight = 1f)
                        TableCell(text = moodText, weight = 1f)
                    }
                }
            }
        }
    }
}

@Composable
fun TableCell(text: String, weight: Float, color: Color? = null) {
    Text(
        text = text,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(8.dp),
        fontWeight = if (color != null) FontWeight.Bold else FontWeight.Normal,
        color = color ?: MaterialTheme.colorScheme.onSurface
    )
}
