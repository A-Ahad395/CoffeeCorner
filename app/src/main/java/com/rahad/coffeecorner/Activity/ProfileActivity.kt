package com.rahad.coffeecorner.Activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rahad.coffeecorner.databinding.ActivityProfileBinding
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.profileImage.setImageURI(uri)
                saveImageToInternalStorage(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance(
            "https://coffeecornerrahad-default-rtdb.firebaseio.com/"
        )

        loadLocalProfileImage()
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

                if (name != "null") {
                    binding.nameEdt.setText(name)
                }
            }
    }

    private fun setListeners() {

        binding.uploadPhotoBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.profileImage.setOnClickListener {
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

    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val folder = File(filesDir, "profile")

            if (!folder.exists()) {
                folder.mkdirs()
            }

            val file = File(folder, "profile.jpg")
            val outputStream = FileOutputStream(file)

            bitmap.compress(
                android.graphics.Bitmap.CompressFormat.JPEG,
                100,
                outputStream
            )

            outputStream.close()

            val prefs = getSharedPreferences(
                "profile_prefs",
                Context.MODE_PRIVATE
            )

            prefs.edit()
                .putString("profile_path", file.absolutePath)
                .apply()

            Toast.makeText(this, "Photo saved", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadLocalProfileImage() {
        val prefs = getSharedPreferences(
            "profile_prefs",
            Context.MODE_PRIVATE
        )

        val path = prefs.getString("profile_path", null)

        if (path != null) {
            val file = File(path)

            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                binding.profileImage.setImageBitmap(bitmap)
            }
        }
    }
}