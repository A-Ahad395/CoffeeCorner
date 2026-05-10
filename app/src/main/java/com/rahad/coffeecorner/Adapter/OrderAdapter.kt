package com.rahad.coffeecorner.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahad.coffeecorner.Domain.OrderModel
import com.rahad.coffeecorner.databinding.ViewholderOrderBinding

class OrderAdapter(
    private val orders: ArrayList<OrderModel>
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]

        holder.binding.dateTxt.text = order.orderDate
        holder.binding.totalTxt.text = "Total Bill: TK-${order.totalCost}"
        holder.binding.itemCountTxt.text = "Items: ${order.items.size}"
        val itemNames = order.items.joinToString(separator = ", ") {
            "${it.title} (TK-${it.price}) x${it.numberInCart}"
        }

        holder.binding.itemNamesTxt.text = itemNames

        holder.binding.itemNamesTxt.text = itemNames
    }

    override fun getItemCount(): Int = orders.size
}