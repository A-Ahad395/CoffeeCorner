package com.rahad.coffeecorner.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.rahad.coffeecorner.ViewModel.MainViewModel
import com.rahad.coffeecorner.databinding.ActivityMainBinding
import com.rahad.coffeecorner.Adapter.CategoryAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.rahad.coffeecorner.Adapter.PopularAdapter
import android.content.Intent
import com.rahad.coffeecorner.Activity.CartActivity
import com.rahad.coffeecorner.Activity.FavoriteActivity
import android.text.Editable
import android.text.TextWatcher
import com.rahad.coffeecorner.Domain.ItemsModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var popularList = mutableListOf<ItemsModel>()
    private val viewModel = MainViewModel()

    private var allItemsList = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seeAllTxt.setOnClickListener {
            val intent = Intent(this, ItemsListActivity::class.java)
            intent.putExtra("showAll", true)
            startActivity(intent)
        }

        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()
        initSearch()
    }

    private fun initBottomMenu() {

        binding.cartBtn.setOnClickListener {

            startActivity(
                Intent(this, CartActivity::class.java)
            )
        }

        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.favBtn.setOnClickListener {

            startActivity(
                Intent(this, FavoriteActivity::class.java)
            )
        }

        binding.myOrderBtn.setOnClickListener {
            startActivity(Intent(this, MyOrderActivity::class.java))
        }

        binding.explorerBtn.setOnClickListener {

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    private fun initBanner() {

        binding.progressBarBanner.visibility = View.VISIBLE

        viewModel.loadBanner().observe(this) {

            if (it.isNotEmpty()) {

                Glide.with(this)
                    .load(it[0].url)
                    .into(binding.banner)
            }

            binding.progressBarBanner.visibility = View.GONE
        }
    }

    private fun initCategory() {

        binding.progressBarCategory.visibility = View.VISIBLE

        binding.recyclerViewCat.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.loadCategory().observe(this) {

            binding.recyclerViewCat.adapter = CategoryAdapter(it)

            binding.progressBarCategory.visibility = View.GONE
        }
    }

    private fun initPopular() {

        binding.progressBarPopular.visibility = View.VISIBLE

        viewModel.loadPopular().observe(this) { list ->

            popularList = list.toMutableList()

            binding.recyclerViewPopular.layoutManager =
                GridLayoutManager(this, 2)

            binding.recyclerViewPopular.adapter =
                PopularAdapter(popularList)

            binding.progressBarPopular.visibility = View.GONE
        }

        viewModel.loadAllItems().observe(this) { list ->
            allItemsList = list.toMutableList()
        }
    }

    private fun initSearch() {

        binding.editTextText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                val query = s.toString().lowercase().trim()

                if (query.isEmpty()) {

                    binding.recyclerViewPopular.adapter =
                        PopularAdapter(popularList)

                } else {

                    val filteredList = allItemsList.filter {
                        it.title.lowercase().contains(query)
                    }.toMutableList()

                    binding.recyclerViewPopular.adapter =
                        PopularAdapter(filteredList)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}