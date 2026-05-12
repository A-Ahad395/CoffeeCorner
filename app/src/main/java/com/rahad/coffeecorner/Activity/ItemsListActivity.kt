package com.rahad.coffeecorner.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahad.coffeecorner.Adapter.ItemsListCategoryAdapter
import com.rahad.coffeecorner.ViewModel.MainViewModel
import com.rahad.coffeecorner.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemsListBinding
    private val viewModel = MainViewModel()

    private var id: String = ""
    private var title: String = ""
    private var showAll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        initList()
    }

    private fun initList() {
        binding.apply {

            progressBar.visibility = View.VISIBLE

            listView.layoutManager =
                LinearLayoutManager(
                    this@ItemsListActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )

            if (showAll) {
                viewModel.loadAllItems().observe(this@ItemsListActivity) {
                    listView.adapter = ItemsListCategoryAdapter(it)
                    progressBar.visibility = View.GONE
                }
            } else {
                viewModel.loadItems(id).observe(this@ItemsListActivity) {
                    listView.adapter = ItemsListCategoryAdapter(it)
                    progressBar.visibility = View.GONE
                }
            }

            backBtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun getBundle() {
        showAll = intent.getBooleanExtra("showAll", false)

        if (showAll) {
            binding.categoryTxt.text = "All Coffee"
        } else {
            id = intent.getStringExtra("id") ?: ""
            title = intent.getStringExtra("title") ?: "Items"
            binding.categoryTxt.text = title
        }
    }
}