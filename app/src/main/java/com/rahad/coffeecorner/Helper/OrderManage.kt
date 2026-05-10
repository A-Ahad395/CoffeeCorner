package com.rahad.coffeecorner.Helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rahad.coffeecorner.Domain.OrderModel

object OrderManager {

    private const val ORDER_KEY = "OrderHistory"

    fun addOrder(context: Context, order: OrderModel) {

        val tinyDB = TinyDB(context)

        val list = getOrders(context)

        list.add(order)

        val json = Gson().toJson(list)

        tinyDB.putString(ORDER_KEY, json)
    }

    fun getOrders(context: Context): ArrayList<OrderModel> {

        val tinyDB = TinyDB(context)

        val json = tinyDB.getString(ORDER_KEY)

        return if (json.isEmpty()) {

            ArrayList()

        } else {

            val type =
                object : TypeToken<ArrayList<OrderModel>>() {}.type

            Gson().fromJson(json, type)
        }
    }
}