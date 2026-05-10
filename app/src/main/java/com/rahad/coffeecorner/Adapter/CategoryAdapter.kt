package com.rahad.coffeecorner.Adapter

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rahad.coffeecorner.Activity.ItemsListActivity
import com.rahad.coffeecorner.Domain.CategoryModel
import com.rahad.coffeecorner.R
import com.rahad.coffeecorner.databinding.ViewholderCategoryBinding

class CategoryAdapter(private val items: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.binding.titleCat.text = item.title

        holder.itemView.setOnClickListener {

            lastSelectedPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            val context = holder.itemView.context

            Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(context, ItemsListActivity::class.java).apply {

                    putExtra("id", item.id.toString())
                    putExtra("title", item.title)
                }

                ContextCompat.startActivity(context, intent, null)

            }, 500)
        }

        if (selectedPosition == position) {

            holder.binding.titleCat.setBackgroundResource(R.drawable.dark_brown_bg)

            holder.binding.titleCat.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.white)
            )

        } else {

            holder.binding.titleCat.setBackgroundResource(R.drawable.white_bg)

            holder.binding.titleCat.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.darkBrown)
            )
        }
    }

    override fun getItemCount(): Int = items.size
}