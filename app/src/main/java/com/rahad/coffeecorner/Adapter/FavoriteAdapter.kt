package com.rahad.coffeecorner.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahad.coffeecorner.Domain.ItemsModel
import com.rahad.coffeecorner.Helper.FavoriteManager
import com.rahad.coffeecorner.databinding.ViewholderCartBinding
import android.widget.Toast
import com.rahad.coffeecorner.Helper.ManagmentCart

class FavoriteAdapter(
    private val items: ArrayList<ItemsModel>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "TK-${item.price}"
        holder.binding.totalEachItem.text = "Add Cart"

        holder.binding.numberItemTxt.visibility = View.GONE
        holder.binding.plusEachItem.visibility = View.GONE
        holder.binding.minusEachItem.visibility = View.GONE
        holder.binding.totalEachItem.setOnClickListener {
            item.numberInCart = 1
            ManagmentCart(holder.itemView.context).insertItems(item)

            Toast.makeText(
                holder.itemView.context,
                "Added to Cart",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (item.picUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .into(holder.binding.picCart)
        }

        holder.binding.remoteItemBtn.setOnClickListener {
            val pos = holder.adapterPosition

            if (pos != RecyclerView.NO_POSITION) {
                FavoriteManager.removeFavorite(holder.itemView.context, pos)
                items.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, items.size)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}