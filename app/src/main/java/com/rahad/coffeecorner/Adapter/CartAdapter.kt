package com.rahad.coffeecorner.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rahad.coffeecorner.Domain.ChangeNumberItemsListener
import com.rahad.coffeecorner.Domain.ItemsModel
import com.rahad.coffeecorner.Helper.ManagmentCart
import com.rahad.coffeecorner.databinding.ViewholderCartBinding
import kotlin.math.roundToInt

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    private val context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val managementCart = ManagmentCart(context)

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
        val item = listItemSelected[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "TK-${item.price}"
        holder.binding.dateTxt.text = item.orderDate
        holder.binding.totalEachItem.text =
            "TK-${(item.numberInCart * item.price).roundToInt()}"
        holder.binding.numberItemTxt.text = item.numberInCart.toString()

        if (item.picUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .apply(RequestOptions().centerCrop())
                .into(holder.binding.picCart)
        }

        holder.binding.plusEachItem.setOnClickListener {
            managementCart.plusItem(
                listItemSelected,
                position,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                }
            )
        }
        holder.binding.minusEachItem.setOnClickListener {
            managementCart.minusItem(
                listItemSelected,
                position,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                }
            )
        }

        holder.binding.remoteItemBtn.setOnClickListener {
            managementCart.removeItem(
                listItemSelected,
                position,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        notifyDataSetChanged()
                        changeNumberItemsListener?.onChanged()
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}