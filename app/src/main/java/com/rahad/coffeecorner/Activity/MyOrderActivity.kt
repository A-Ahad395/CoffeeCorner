package com.rahad.coffeecorner.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rahad.coffeecorner.Adapter.OrderAdapter
import com.rahad.coffeecorner.Domain.OrderModel
import com.rahad.coffeecorner.databinding.ActivityMyOrderBinding

class MyOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.orderView.layoutManager =
            LinearLayoutManager(this)

        loadOrdersFromFirebase()
    }

    private fun loadOrdersFromFirebase() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Orders")
            .get()
            .addOnSuccessListener { snapshot ->

                val orders = ArrayList<OrderModel>()

                for (child in snapshot.children) {
                    val order = child.getValue(OrderModel::class.java)

                    if (order != null) {
                        orders.add(order)
                    }
                }

                orders.reverse()

                binding.orderView.adapter =
                    OrderAdapter(orders)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed to load orders: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}