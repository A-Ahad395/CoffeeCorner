package com.rahad.coffeecorner.Helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rahad.coffeecorner.Domain.ItemsModel

object FavoriteManager {

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun addFavorite(item: ItemsModel) {
        val uid = getUserId() ?: return

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Favorites")
            .child(item.title)
            .setValue(item)
    }

    fun removeFavorite(title: String) {
        val uid = getUserId() ?: return

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Favorites")
            .child(title)
            .removeValue()
    }

    fun isFavorite(
        title: String,
        callback: (Boolean) -> Unit
    ) {
        val uid = getUserId()

        if (uid == null) {
            callback(false)
            return
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Favorites")
            .child(title)
            .get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun getFavorites(
        callback: (ArrayList<ItemsModel>) -> Unit
    ) {
        val uid = getUserId()

        if (uid == null) {
            callback(ArrayList())
            return
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Favorites")
            .get()
            .addOnSuccessListener { snapshot ->

                val list = ArrayList<ItemsModel>()

                for (child in snapshot.children) {
                    val item = child.getValue(ItemsModel::class.java)
                    if (item != null) {
                        list.add(item)
                    }
                }

                callback(list)
            }
            .addOnFailureListener {
                callback(ArrayList())
            }
    }
}