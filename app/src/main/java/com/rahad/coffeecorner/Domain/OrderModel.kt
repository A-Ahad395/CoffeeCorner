package com.rahad.coffeecorner.Domain

import java.io.Serializable

data class OrderModel(
    var orderDate: String = "",
    var totalCost: Double = 0.0,
    var items: ArrayList<ItemsModel> = ArrayList()
) : Serializable