package com.example.brainboost

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainboost.ui.theme.BrainBoostTheme

class TipDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("tipTitle") ?: "No Title"
        val content = intent.getStringExtra("tipContent") ?: "No Content"
        val author = intent.getStringExtra("tipAuthor") ?: "Unknown Author"
        val articleUrl = intent.getStringExtra("tipArticleUrl") ?: "https://example.com"

        setContent {
            BrainBoostTheme {
                TipDetailsScreen(title = title, content = content, author = author, articleUrl = articleUrl)
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipDetailsScreen(title: String, content: String, author: String, articleUrl: String) {
    val activity = (LocalContext.current as? ComponentActivity)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Details") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) { // Call finish() to navigate back
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Center content vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Center the title and content horizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center // Center text
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center // Center text
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "- $author",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center // Center text
                )
            }
            Spacer(modifier = Modifier.weight(1f)) // This will push the button towards the center vertically
            Button(
                onClick = {
                    if (title == "Set goals and priorities") { // For Tip 2 specifically
                        activity?.let { activity ->
                            try {
                                // Launch friend's app using package name (replace with the actual package name)
                                val intent = Intent().apply{
                                    component = ComponentName(
                                        "com.example.goalsetting",
                                        "com.example.goalsetting.MainActivity"
                                    )
                                }
                                if (intent != null) {
                                    activity.startActivity(intent)
                                } else {
                                    // Optionally show a toast if app is not found
                                    Toast.makeText(activity, "App not installed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(activity, "Error launching the app", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Open article link if it's not Tip 2
                        activity?.let { activity ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                            activity.startActivity(intent)
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally) // Center the button horizontally
            ) {
                Text(if (title == "Set goals and priorities") "Set Your Goal" else "Read Article")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTipDetailsScreen() {
    BrainBoostTheme {
        TipDetailsScreen(
            title = "Coping with Anxiety",
            content = "Practice deep breathing exercises to calm your mind.",
            author = "John Doe",
            articleUrl = "https://example.com/anxiety-tips"
        )
    }
}