package com.rahad.coffeecorner.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rahad.coffeecorner.Domain.BannerModel
import com.rahad.coffeecorner.Domain.CategoryModel
import com.rahad.coffeecorner.Repository.MainRepository
import com.rahad.coffeecorner.Domain.ItemsModel

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }
    fun loadAllItems() = repository.loadAllItems()
    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }
    fun loadItems(categoryId: String): LiveData<MutableList<ItemsModel>> {
        return repository.loadItemCategory(categoryId)
    }
}