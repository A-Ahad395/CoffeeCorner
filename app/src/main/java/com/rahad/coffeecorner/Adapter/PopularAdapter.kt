package com.rahad.coffeecorner.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahad.coffeecorner.Domain.ItemsModel
import com.rahad.coffeecorner.databinding.ViewholderPopularBinding

class PopularAdapter(
    private val items: MutableList<ItemsModel>
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderPopularBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "TK-${item.price}"

        if (item.picUrl.isNotEmpty()) {
            val imageUrl = item.picUrl[0]

            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.binding.pic)
        }

        holder.itemView.setOnClickListener {
            val intent = android.content.Intent(
                holder.itemView.context,
                com.rahad.coffeecorner.Activity.DetailActivity::class.java
            )
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}