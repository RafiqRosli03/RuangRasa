package com.example.ruangrasaa

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class BreathingExercise : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_breathing_exercise)

        // Reference UI elements
        val tvMusicTitle = findViewById<TextView>(R.id.tvMusicTitle)
        val btnPlayPause = findViewById<ImageButton>(R.id.btnPlayPause)
        val btnStop = findViewById<Button>(R.id.btnStop)

        // Set music title
        tvMusicTitle.text = "Breathing Exercise"

        // Initialize MediaPlayer with a local file
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music)

        // Set initial button background to play icon
        btnPlayPause.setBackgroundResource(R.drawable.play)

        btnPlayPause.setOnClickListener {
            if (!isPlaying) {
                // Start playing and update icon to pause
                mediaPlayer.start()
                btnPlayPause.setBackgroundResource(R.drawable.pause)
                isPlaying = true
            } else {
                // Pause and update icon to play
                mediaPlayer.pause()
                btnPlayPause.setBackgroundResource(R.drawable.play)
                isPlaying = false
            }
        }

        btnStop.setOnClickListener {
            if (isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepare() // Prepare the MediaPlayer again
                btnPlayPause.setBackgroundResource(R.drawable.play)
                isPlaying = false
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
