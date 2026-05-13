package com.rahad.coffeecorner.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rahad.coffeecorner.Domain.ItemsModel
import com.rahad.coffeecorner.Helper.FavoriteManager
import com.rahad.coffeecorner.Helper.ManagmentCart
import com.rahad.coffeecorner.R
import com.rahad.coffeecorner.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagmentCart

    private var numberOrder = 1
    private var sizeMultiplier = 1.0
    private var basePrice = 0.0
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.priceTxt.text =
            "TK-${String.format("%.2f", totalPrice)}"
    }

    private fun bundle() {

        binding.apply {

            item = intent.getSerializableExtra("object") as ItemsModel

            basePrice = item.price

            isFavorite =
                FavoriteManager.isFavorite(
                    this@DetailActivity,
                    item.title
                )

            if (isFavorite) {

                favBtn.setImageResource(R.drawable.ic_heart_red)

            } else {

                favBtn.setImageResource(R.drawable.ic_heart_white)
            }

            if (item.picUrl.isNotEmpty()) {

                Glide.with(this@DetailActivity)
                    .load(item.picUrl[0])
                    .into(picMain)
            }

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "TK-${item.price}"
            ratingTxt.text = item.rating.toString()

            minusEachItem.text = numberOrder.toString()

            backBtn.setOnClickListener {
                finish()
            }

            plusCart.setOnClickListener {

                numberOrder++

                minusEachItem.text =
                    numberOrder.toString()

                updatePrice()
            }

            minusBtn.setOnClickListener {

                if (numberOrder > 1) {

                    numberOrder--

                    minusEachItem.text =
                        numberOrder.toString()

                    updatePrice()
                }
            }

            favBtn.setOnClickListener {

                isFavorite = !isFavorite

                if (isFavorite) {

                    FavoriteManager.addFavorite(
                        this@DetailActivity,
                        item
                    )

                    favBtn.setImageResource(
                        R.drawable.ic_heart_red
                    )

                    Toast.makeText(
                        this@DetailActivity,
                        "Added to Favorite",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    FavoriteManager.removeFavorite(
                        this@DetailActivity,
                        item.title
                    )

                    favBtn.setImageResource(
                        R.drawable.ic_heart_white
                    )

                    Toast.makeText(
                        this@DetailActivity,
                        "Removed from Favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            orderNowBtn.setOnClickListener {

                item.numberInCart = numberOrder

                item.price =
                    basePrice * sizeMultiplier

                managementCart.insertItems(item)

                startActivity(
                    Intent(
                        this@DetailActivity,
                        CartActivity::class.java
                    )
                )
            }

            addToCartBtn.setOnClickListener {

                item.numberInCart = numberOrder

                item.price =
                    basePrice * sizeMultiplier

                managementCart.insertItems(item)

                Toast.makeText(
                    this@DetailActivity,
                    "Added to Cart",
                    Toast.LENGTH_SHORT
                ).show()
            }

            updatePrice()
        }
    }
}