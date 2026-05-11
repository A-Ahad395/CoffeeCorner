package com.rahad.coffeecorner.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.rahad.coffeecorner.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.profileImage.setImageURI(uri)
                uploadProfileImage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://coffeecornerrahad-default-rtdb.firebaseio.com/")
        storage = FirebaseStorage.getInstance("gs://coffeecornerrahad.firebasestorage.app")

        loadUserData()
        setListeners()
    }

    private fun loadUserData() {
        val user = auth.currentUser ?: return
        val uid = user.uid

        binding.emailTxt.text = user.email ?: "No Email"

        database.getReference("Users")
            .child(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value.toString()
                val profileImage = snapshot.child("profileImage").value.toString()

                binding.nameEdt.setText(name)

                if (profileImage.isNotEmpty()) {
                    Glide.with(this)
                        .load(profileImage)
                        .into(binding.profileImage)
                }
            }
    }

    private fun setListeners() {

        binding.uploadPhotoBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.saveNameBtn.setOnClickListener {
            val user = auth.currentUser ?: return@setOnClickListener
            val newName = binding.nameEdt.text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            database.getReference("Users")
                .child(user.uid)
                .child("name")
                .setValue(newName)
                .addOnSuccessListener {
                    Toast.makeText(this, "Name updated", Toast.LENGTH_SHORT).show()
                }
        }
        binding.backBtn.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.savePasswordBtn.setOnClickListener {
            val user = auth.currentUser ?: return@setOnClickListener
            val newPassword = binding.passwordEdt.text.toString().trim()

            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            user.updatePassword(newPassword)
                .addOnSuccessListener {
                    binding.passwordEdt.text.clear()
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }


    private fun uploadProfileImage() {
        val user = auth.currentUser ?: return
        val uri = imageUri ?: return

        val imageRef = storage.reference
            .child("ProfileImages")
            .child("${user.uid}.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    database.getReference("Users")
                        .child(user.uid)
                        .child("profileImage")
                        .setValue(downloadUrl.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }
}