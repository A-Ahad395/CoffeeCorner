package com.rahad.coffeecorner.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahad.coffeecorner.Adapter.FavoriteAdapter
import com.rahad.coffeecorner.Helper.FavoriteManager
import com.rahad.coffeecorner.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.favoriteView.layoutManager =
            LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        FavoriteManager.getFavorites { list ->

            binding.favoriteView.adapter =
                FavoriteAdapter(list)
        }
    }
}