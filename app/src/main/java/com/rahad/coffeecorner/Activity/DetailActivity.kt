package com.rahad.coffeecorner.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rahad.coffeecorner.Domain.ItemsModel
import com.rahad.coffeecorner.Helper.ManagmentCart
import com.rahad.coffeecorner.databinding.ActivityDetailBinding
import com.rahad.coffeecorner.R
import android.widget.Toast
import com.rahad.coffeecorner.Helper.FavoriteManager
import android.content.Intent
import android.widget.Button

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagmentCart
    private var numberOrder = 1
    private var sizeMultiplier = 1.0
    private var basePrice = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderNowBtn = findViewById<Button>(R.id.orderNowBtn)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        bundle()
        initSizeList()
    }

    private fun initSizeList() {
        binding.apply {

            smallBtn.setOnClickListener {
                sizeMultiplier = 1.0
                smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
                updatePrice()
            }

            mediumBtn.setOnClickListener {
                sizeMultiplier = 1.2
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                largeBtn.setBackgroundResource(0)
                updatePrice()
            }

            largeBtn.setOnClickListener {
                sizeMultiplier = 1.5
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                updatePrice()
            }
        }
    }
    private fun updatePrice() {
        val totalPrice = basePrice * sizeMultiplier * numberOrder
        binding.priceTxt.text = "TK-$totalPrice"
    }
    private fun bundle() {

        binding.apply {

            // get data
            item = intent.getSerializableExtra("object") as ItemsModel
            basePrice = item.price

            // image load
            if (item.picUrl.isNotEmpty()) {
                Glide.with(this@DetailActivity)
                    .load(item.picUrl[0])
                    .into(picMain)   // ⚠️ তোমার XML এ picMain
            }

            // set data
            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "TK-${item.price}"
            ratingTxt.text = item.rating.toString()

            minusEachItem.text = numberOrder.toString()

            // back button
            backBtn.setOnClickListener {
                finish()
            }

            // plus
            plusCart.setOnClickListener {
                numberOrder++
                minusEachItem.text = numberOrder.toString()
                updatePrice()
            }

            // minus
            minusBtn.setOnClickListener {
                if (numberOrder > 1) {
                    numberOrder--
                    minusEachItem.text = numberOrder.toString()
                    updatePrice()
                }
            }
            favBtn.setOnClickListener {
                FavoriteManager.addFavorite(this@DetailActivity, item)
                Toast.makeText(this@DetailActivity, "Added to Favorite", Toast.LENGTH_SHORT).show()
            }
            orderNowBtn.setOnClickListener {

                item.numberInCart = numberOrder

                managementCart.insertItems(item)

                startActivity(
                    Intent(this@DetailActivity, CartActivity::class.java)
                )
            }

            // add to cart
            addToCartBtn.setOnClickListener {
                item.numberInCart = numberOrder
                item.price = basePrice * sizeMultiplier
                managementCart.insertItems(item)
            }
            updatePrice()
        }
    }
}