package com.example.ruangrasaa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ruangrasaa.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        userId = auth.currentUser?.uid

        if (userId != null) {
            observeUserProfile()
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Save button action
        binding.saveButton.setOnClickListener {
            saveUserProfile()
        }

        // Cancel button action
        binding.cancelButton.setOnClickListener {
            finish() // Close the activity
        }
        // Logout button
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Launch Edit Profile Activity
        binding.editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    private fun observeUserProfile() {
        userId?.let { id ->
            firestore.collection("profile").document(id)
                .addSnapshotListener { document, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    if (document != null && document.exists()) {
                        binding.nameEditText.setText(document.getString("name"))
                        binding.addressEditText.setText(document.getString("address"))
                        binding.bioEditText.setText(document.getString("bio"))
                        binding.ageEditText.setText(document.getLong("age")?.toString())
                    } else {
                        return@addSnapshotListener
                    }
                }
        }
    }

    private fun saveUserProfile() {
        val name = binding.nameEditText.text.toString().trim()
        val address = binding.addressEditText.text.toString().trim()
        val bio = binding.bioEditText.text.toString().trim()
        val age = binding.ageEditText.text.toString().trim()

        if (name.isEmpty() || address.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val profileData = mapOf(
            "name" to name,
            "address" to address,
            "bio" to bio,
            "age" to (age.toIntOrNull() ?: 0) // Default to 0 if age is invalid
        )

        userId?.let { id ->
            firestore.collection("profile").document(id)
                .set(profileData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
