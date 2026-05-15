package com.rahad.coffeecorner.Helper

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rahad.coffeecorner.Domain.ChangeNumberItemsListener
import com.rahad.coffeecorner.Domain.ItemsModel

class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun saveCartToFirebase(list: ArrayList<ItemsModel>) {
        val uid = getUserId()

        if (uid == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Cart")
            .setValue(list)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                Toast.makeText(context, "Cart save failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun insertItems(item: ItemsModel) {
        val listItem = getListCart()

        val existAlready = listItem.any { it.title == item.title }
        val index = listItem.indexOfFirst { it.title == item.title }

        if (existAlready) {
            listItem[index].numberInCart = item.numberInCart
        } else {
            listItem.add(item)
        }

        tinyDB.putListObject("CartList", listItem)

        saveCartToFirebase(listItem)

        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(
        listItems: ArrayList<ItemsModel>,
        position: Int,
        listener: ChangeNumberItemsListener
    ) {
        if (listItems[position].numberInCart == 1) {
            listItems.removeAt(position)
        } else {
            listItems[position].numberInCart--
        }

        tinyDB.putListObject("CartList", listItems)

        saveCartToFirebase(listItems)

        listener.onChanged()
    }

    fun removeItem(
        listItems: ArrayList<ItemsModel>,
        position: Int,
        listener: ChangeNumberItemsListener
    ) {
        listItems.removeAt(position)

        tinyDB.putListObject("CartList", listItems)

        saveCartToFirebase(listItems)

        listener.onChanged()
    }

    fun plusItem(
        listItems: ArrayList<ItemsModel>,
        position: Int,
        listener: ChangeNumberItemsListener
    ) {
        listItems[position].numberInCart++

        tinyDB.putListObject("CartList", listItems)

        saveCartToFirebase(listItems)

        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listItem = getListCart()
        var fee = 0.0

        for (item in listItem) {
            fee += item.price * item.numberInCart
        }

        return fee
    }

    fun clearCart() {
        tinyDB.remove("CartList")

        val uid = getUserId() ?: return

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Cart")
            .removeValue()
    }
}