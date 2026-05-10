package com.rahad.coffeecorner.Helper

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rahad.coffeecorner.Domain.ItemsModel

object FavoriteManager {

    private const val FAVORITE_KEY = "favorite_items"

    fun addFavorite(context: Context, item: ItemsModel) {

        val list = getFavorites(context)

        if (!list.any { it.title == item.title }) {

            list.add(item)

            saveFavorites(context, list)
        }
    }

    fun getFavorites(context: Context): ArrayList<ItemsModel> {

        val tinyDB = TinyDB(context)

        val json = tinyDB.getString(FAVORITE_KEY)

        return if (json.isEmpty()) {

            ArrayList()

        } else {

            val type = object : TypeToken<ArrayList<ItemsModel>>() {}.type

            Gson().fromJson(json, type)
        }
    }

    fun removeFavorite(context: Context, position: Int) {

        val list = getFavorites(context)

        list.removeAt(position)

        saveFavorites(context, list)
    }

    private fun saveFavorites(
        context: Context,
        list: ArrayList<ItemsModel>
    ) {

        val tinyDB = TinyDB(context)

        val json = Gson().toJson(list)

        tinyDB.putString(FAVORITE_KEY, json)
    }
}