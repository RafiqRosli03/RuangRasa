package com.example.ruangrasaa

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ruangrasaa.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid

        // Fetch and display user data in real-time
        if (userId != null) {
            firestore.collection("profile").document(userId).addSnapshotListener { document, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (document != null && document.exists()) {
                    val userInfo =
                        "Name       : ${document.getString("name") ?: "N/A"}\n" +
                        "Address   : ${document.getString("address") ?: "N/A"}\n" +
                        "Bio            : ${document.getString("bio") ?: "N/A"}\n" +
                        "Age           : ${document.getLong("age")?.toString() ?: "N/A"}"
                    binding.userInfo.text = userInfo
                } else {
                    binding.userInfo.text = "No user data available"
                }
            }
        }

        // Logout button
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Navigate to Breathing Exercise
        binding.BreathingExerciseBtn.setOnClickListener {
            val intent = Intent(this, BreathingExercise::class.java)
            startActivity(intent)
        }

        // Launch Goal Setting App
        binding.GoalsettingBtn.setOnClickListener {
            launchExternalApp("com.example.goalsetting", "com.example.goalsetting.MainActivity")
        }

        // Launch Brain Boost App
        binding.BrainBoostBtn.setOnClickListener {
            launchExternalApp("com.example.brainboost", "com.example.brainboost.MainActivity")
        }

        // Launch Mood Tracking App
        binding.MoodtrackingBtn.setOnClickListener {
            launchExternalApp("com.example.moodtracking22", "com.example.moodtracking22.MainActivity")
        }

        // Launch Edit Profile Activity
        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun launchExternalApp(packageName: String, className: String) {
        try {
            val intent = Intent().apply {
                component = ComponentName(packageName, className)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "App not installed", Toast.LENGTH_SHORT).show()
        }
    }
}
