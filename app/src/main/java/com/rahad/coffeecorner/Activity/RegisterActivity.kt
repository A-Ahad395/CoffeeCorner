package com.rahad.coffeecorner.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rahad.coffeecorner.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val nameEdt = findViewById<EditText>(R.id.nameEdt)
        val emailEdt = findViewById<EditText>(R.id.emailEdt)
        val passwordEdt = findViewById<EditText>(R.id.passwordEdt)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {

            val name = nameEdt.text.toString().trim()
            val email = emailEdt.text.toString().trim()
            val password = passwordEdt.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val uid = auth.currentUser!!.uid

                        val userMap = hashMapOf(
                            "uid" to uid,
                            "name" to name,
                            "email" to email,
                            "profileImage" to "",
                            "totalPurchase" to 0.0
                        )

                        FirebaseDatabase
                            .getInstance("https://coffeecornerrahad-default-rtdb.firebaseio.com/")
                            .getReference("Users")
                            .child(uid)
                            .setValue(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, SplashActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            }

                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

        findViewById<LinearLayout>(R.id.backBtn).setOnClickListener {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }
}