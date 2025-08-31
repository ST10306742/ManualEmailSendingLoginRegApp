package com.example.loginregwithotp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginregwithotp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class Home_Activity : AppCompatActivity() {

    // ViewBinding reference
    private lateinit var binding: ActivityHomeBinding

    // FirebaseAuth reference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate layout with ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Logout button click
        binding.btnLogout.setOnClickListener {
            auth.signOut() // Sign the user out of Firebase

            // Redirect back to login activity (or main screen)
            val intent = Intent(this, MainActivity::class.java) // Replace with your login activity
            startActivity(intent)
            finish() // Close Home_Activity
        }
    }
}
