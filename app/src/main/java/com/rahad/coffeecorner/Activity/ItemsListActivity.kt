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

            viewModel.loadItems(id).observe(this@ItemsListActivity) {

                listView.layoutManager =
                    LinearLayoutManager(
                        this@ItemsListActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )

                listView.adapter = ItemsListCategoryAdapter(it)

                progressBar.visibility = View.GONE
            }

            backBtn.setOnClickListener {
                finish()
            }
        }
    }

    private fun getBundle() {

        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!

        binding.categoryTxt.text = title
    }
}