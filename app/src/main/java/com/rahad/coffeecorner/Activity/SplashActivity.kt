package com.rahad.coffeecorner.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rahad.coffeecorner.databinding.ActivitySplashBinding
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createAccountTxt.setOnClickListener {

            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }

        binding.startBtn.setOnClickListener {

            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Login Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}