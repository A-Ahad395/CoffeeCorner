package com.rahad.coffeecorner.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahad.coffeecorner.Adapter.OrderAdapter
import com.rahad.coffeecorner.Helper.OrderManager
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

        binding.orderView.layoutManager = LinearLayoutManager(this)

        val orders = OrderManager.getOrders(this)
        orders.reverse()

        binding.orderView.adapter = OrderAdapter(orders)
    }
}