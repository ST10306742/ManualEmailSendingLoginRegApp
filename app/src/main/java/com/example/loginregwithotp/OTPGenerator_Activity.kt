package com.example.loginregwithotp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.loginregwithotp.databinding.ActivityOtpgeneratorBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random
import kotlin.random.nextInt

class OTPGenerator_Activity : AppCompatActivity() {

    // Declare binding and auth so they are accessible in whole class
    private lateinit var binding: ActivityOtpgeneratorBinding
    private lateinit var auth: FirebaseAuth

    // Store the OTP that was sent via email so we can validate later
    private var generatedOTP: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        enableEdgeToEdge()
        setContentView(R.layout.activity_otpgenerator)

        // Apply system window insets (status bar, nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize binding
        binding = ActivityOtpgeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get email + password from previous activity
        val email = intent.getStringExtra("email").toString()
        val password = intent.getStringExtra("password").toString()

        // Generate & send OTP to user's email
        generatedOTP = randomOTPGenerator(email).toString()

        // Show the email in TextView for user reference
        binding.tvShowEmail.text = email

        // Allow user to request a new OTP
        binding.tvResend.setOnClickListener {
            generatedOTP = randomOTPGenerator(email).toString()
        }

        // =============================
        // OTP Box Navigation
        // =============================

        binding.etOtp1.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) binding.etOtp2.requestFocus()
        }

        binding.etOtp2.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) binding.etOtp3.requestFocus()
            else binding.etOtp1.requestFocus()
        }

        binding.etOtp3.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) binding.etOtp4.requestFocus()
            else binding.etOtp2.requestFocus()
        }

        binding.etOtp4.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) binding.etOtp5.requestFocus()
            else binding.etOtp3.requestFocus()
        }

        binding.etOtp5.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) binding.etOtp6.requestFocus()
            else binding.etOtp4.requestFocus()
        }

        binding.etOtp6.doOnTextChanged { text, _, _, _ ->
            if (text!!.isEmpty()) binding.etOtp5.requestFocus()
        }

        // =============================
        // Verify OTP Button
        // =============================
        binding.btnVerify.setOnClickListener {
            // Combine digits from all OTP boxes
            val enteredOTP = binding.etOtp1.text.toString() +
                    binding.etOtp2.text.toString() +
                    binding.etOtp3.text.toString() +
                    binding.etOtp4.text.toString() +
                    binding.etOtp5.text.toString() +
                    binding.etOtp6.text.toString()

            // Check if all fields are filled
            if (enteredOTP.length != 6) {
                Toast.makeText(this, "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Compare entered OTP with generated OTP
            if (enteredOTP != generatedOTP) {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            } else {
                // If OTP is correct, register user in Firebase
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, Home_Activity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            it.exception?.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    // =====================================================
    // Function to generate random OTP and send via Gmail
    // =====================================================
    private fun randomOTPGenerator(receiverEmail: String): Int {
        val randomOTP = Random.nextInt(100000..999999)

        // Run email sending in background (IO Thread)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val props = Properties().apply {
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.socketFactory.port", "465")
                    put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.port", "465")
                }

                // Authenticate with Gmail (App Password required)
                val session = Session.getDefaultInstance(props,
                    object : javax.mail.Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(
                                "ayush07.mahabeer@gmail.com",    // 🔹 Replace with your Gmail
                                "wtll ubmd mbdq cjmk"       // 🔹 Replace with 16-char App Password
                            )
                        }
                    })

                // Create the email message
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress("ayush07.mahabeer@gmail.com")) // sender
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail)) // receiver
                    subject = "Login Reg OTP from Ayush's practice app" // subject
                    setText("Your lovely OTP is: $randomOTP ... yay !!! ;)") // message body
                }

                // Send the email
                Transport.send(message)

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        this@OTPGenerator_Activity,
                        "Failed to send OTP: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return randomOTP
    }
}
