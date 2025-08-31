package com.example.loginregwithotp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginregwithotp.databinding.ActivityRegisterBinding

class Register_Activity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //when user clicks 'Register' button
        binding.btnRegister.setOnClickListener {
            //Input Validations
            if(binding.etEmail.text.toString().isEmpty())
            {
                Toast.makeText(
                        this,
                        "Please enter your email.", //check if email was entered
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
            {
                if(binding.etPassword.text.toString().isEmpty())
                {
                    Toast.makeText(
                        this,
                        "Please enter your password.",  //check if password was entered
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else
                {
                    if(binding.etConfirmpassword.text.toString().isEmpty())
                    {
                        Toast.makeText(
                            this,
                            "Please confirm your password.",    //check if confirmed password was entered
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else
                    {
                        //check if both passwords match
                        if(binding.etPassword.text.toString() != binding.etConfirmpassword.text.toString())
                        {
                            Toast.makeText(
                                this,
                                "Ensure your password and confirmed password matches.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else
                        {
                            //send user to OTP activity
                            var intent = Intent(this@Register_Activity, OTPGenerator_Activity::class.java)
                            intent.putExtra("email",binding.etEmail.text.toString())
                            intent.putExtra("password", binding.etPassword.text.toString())
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}