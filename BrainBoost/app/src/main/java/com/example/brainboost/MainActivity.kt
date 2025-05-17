package com.example.brainboost

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainboost.ui.theme.BrainBoostTheme
import java.util.Locale

// Main Activity
class MainActivity : ComponentActivity() {
    private val REQUEST_CODE_VOICE_RECOGNITION = 100
    private var searchQueryState by mutableStateOf("")

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrainBoostTheme {
                val context = LocalContext.current

                Scaffold(
                    topBar = {
                        TopAppBarWithBackButton(
                            title = "Brain Boost",
                            onBackClick = { finish() }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val tipsRepository = TipsRepository()
                    initializeSampleTips(tipsRepository)

                    TipsScreen(
                        tipsRepository = tipsRepository,
                        modifier = Modifier.padding(innerPadding),
                        onTipClick = { tip ->
                            // Handle click event, e.g., navigate to detail screen
                            val intent = Intent(context, TipDetailsActivity::class.java).apply {
                                putExtra("tipTitle", tip.title)
                                putExtra("tipContent", tip.content)
                                putExtra("tipAuthor", tip.author)
                                putExtra("tipArticleUrl", tip.articleUrl)
                            }
                            context.startActivity(intent)
                        },
                        onVoiceSearch = {
                            startVoiceRecognition()
                        },
                        searchQuery = searchQueryState,
                        onSearchQueryChange = { query -> searchQueryState = query }
                    )
                }
            }
        }
    }

    private fun startVoiceRecognition() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
            }
            startActivityForResult(intent, REQUEST_CODE_VOICE_RECOGNITION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE_RECOGNITION && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            results?.let {
                // Update the search query with voice input
                searchQueryState = it[0]
            }
        }
    }

    private fun initializeSampleTips(tipsRepository: TipsRepository) {
        tipsRepository.addTip(Tip("tip1", "Coping with Anxiety", "Practice deep breathing exercises to calm your mind.", "Show Details", "https://www.healthline.com/health/breathing-exercises-for-anxiety#long-exhale"))
        tipsRepository.addTip(Tip("tip2", "Set goals and priorities", "Decide what must get done now and what can wait.", "Go to activity", "https://example.com/goal-setting-tips"))
        tipsRepository.addTip(Tip("tip3", "Workout", "Don't miss your workout sessions for a healthy body and mind.", "Show Details", "https://www.healthline.com/nutrition/10-benefits-of-exercise#weight-management"))
        tipsRepository.addTip(Tip("tip4", "Make sleep a priority", "Stick to a schedule, and make sure you're getting enough sleep.", "Show Details", "https://www.psychiatry.org/news-room/apa-blogs/making-sleep-a-priority-for-mental-well-being"))
        tipsRepository.addTip(Tip("tip5", "Stay Connected", "Reach out to friends or family members who can provide emotional support and practical help.", "Show Details", "https://www.mayoclinic.org/healthy-lifestyle/stress-management/in-depth/support-groups/art-20044655"))
        tipsRepository.addTip(Tip("tip6", "Eat healthy, regular meals and stay hydrated", "A balanced diet and plenty of water can improve your energy and focus throughout the day.", "Show Details", "https://www.nhs.uk/live-well/eat-well/how-to-eat-a-balanced-diet/eight-tips-for-healthy-eating/"))
    }
}

// Data class for Tips
data class Tip(
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val articleUrl: String
)

// Repository for managing tips
class TipsRepository {
    private val tips = mutableListOf<Tip>()

    fun addTip(tip: Tip) {
        tips.add(tip)
    }

    fun getAllTips(): List<Tip> = tips
}

// UI Components
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithBackButton(
    title: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                //val packageName = "com.example." // Replace with your friend's app package name
                //val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                val intent = Intent().apply{
                    component = ComponentName(
                        "com.example.ruangrasaA",
                        "com.example.ruangrasaA.MainActivity"
                    )
                }
                if (intent != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "App not installed", Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(
    tipsRepository: TipsRepository,
    modifier: Modifier = Modifier,
    onTipClick: (Tip) -> Unit,
    onVoiceSearch: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val allTips = tipsRepository.getAllTips()
    val filteredTips = allTips.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.content.contains(searchQuery, ignoreCase = true) ||
                it.author.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Search bar with integrated voice search button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("Search") },
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(onClick = onVoiceSearch) {
                        Icon(
                            painter = painterResource(id = R.drawable.mic),
                            contentDescription = "Voice Search"
                        )
                    }
                }
            )
        }

        // Display tips
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredTips) { tip ->
                TipCard(tip = tip, onTipClick = onTipClick)
            }
        }
    }
}

@Composable
fun TipCard(tip: Tip, onTipClick: (Tip) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onTipClick(tip) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tip.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = tip.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                textAlign = TextAlign.Justify
            )
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            Text(
                text = "- ${tip.author}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTipsScreen() {
    BrainBoostTheme {
        val tipsRepository = TipsRepository().apply {
            addTip(Tip("tip1", "Coping with Anxiety", "Practice deep breathing exercises to calm your mind.", "Go to activity", "https://example.com/anxiety-tips"))
            addTip(Tip("tip2", "Set goals and priorities", "Decide what must get done now and what can wait.", "Go to activity", "https://example.com/goal-setting-tips"))
            addTip(Tip("tip3", "Workout", "Stay consistent with your workouts to maintain a healthy lifestyle.", "Show Details", "https://example.com/workout-tips"))
        }
        TipsScreen(
            tipsRepository = tipsRepository,
            onTipClick = {},
            onVoiceSearch = {},
            searchQuery = "",
            onSearchQueryChange = {}
        )
    }
}