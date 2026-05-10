package com.rahad.coffeecorner.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahad.coffeecorner.Adapter.CartAdapter
import com.rahad.coffeecorner.Domain.ChangeNumberItemsListener
import com.rahad.coffeecorner.Domain.OrderModel
import com.rahad.coffeecorner.Helper.ManagmentCart
import com.rahad.coffeecorner.Helper.OrderManager
import com.rahad.coffeecorner.databinding.ActivityCartBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round
import android.content.Intent
import android.os.Handler
import android.os.Looper

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagmentCart
    private var tax: Double = 0.0
    private var total: Double = 0.0

    private var discount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        calculateCart()
        setVariable()
        initCartList()
    }

    private fun initCartList() {
        binding.listView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.listView.adapter = CartAdapter(
            managementCart.getListCart(),
            this,
            object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            }
        )
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.orderconfirmBtn.setOnClickListener {
            val orderList = managementCart.getListCart()

            if (orderList.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Order Confirmed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate = SimpleDateFormat(
                "dd MMM yyyy hh:mm a",
                Locale.getDefault()
            ).format(Date())

            val order = OrderModel(
                orderDate = currentDate,
                totalCost = total,
                items = orderList
            )

            OrderManager.addOrder(this, order)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MyOrderActivity::class.java))
                finish()
            }, 2000)
            managementCart.clearCart()
            initCartList()
            calculateCart()
            Toast.makeText(this, "Order Confirmed", Toast.LENGTH_SHORT).show()
        }
        binding.applyBtn.setOnClickListener {
            applyCoupon(binding.editTextText2.text.toString().trim())
        }
    }
    private fun applyCoupon(couponCode: String) {
        val tinyDB = com.rahad.coffeecorner.Helper.TinyDB(this)
        val code = couponCode.uppercase()

        if (code.isEmpty()) {
            Toast.makeText(this, "Enter coupon code", Toast.LENGTH_SHORT).show()
            return
        }

        val usedCoupons = tinyDB.getListString("UsedCoupons")

        if (usedCoupons.contains(code)) {
            Toast.makeText(this, "Coupon already used", Toast.LENGTH_SHORT).show()
            return
        }

        val subTotal = managementCart.getTotalFee()

        discount = when (code) {

            "COFFEE10" -> subTotal * 0.10
            "WELCOME15" -> subTotal * 0.15
            "RAHAD20" -> subTotal * 0.20
            "LATTE25" -> subTotal * 0.25
            "MOCHA30" -> subTotal * 0.30
            "ESPRESSO5" -> subTotal * 0.05
            "CAPPUCCINO12" -> subTotal * 0.12
            "BEANS18" -> subTotal * 0.18
            "HOTBREW22" -> subTotal * 0.22
            "COLDCOFFEE14" -> subTotal * 0.14
            "SUMMER20" -> subTotal * 0.20
            "WINTER25" -> subTotal * 0.25
            "NEWUSER30" -> subTotal * 0.30
            "FIRSTORDER01" -> subTotal * 0.35
            "FREESHIP10" -> subTotal * 0.10
            "DARKROAST15" -> subTotal * 0.15
            "MILKFOAM8" -> subTotal * 0.08
            "AFFOGATO16" -> subTotal * 0.16
            "COFFEELOVER28" -> subTotal * 0.28
            "CAFE50" -> subTotal * 0.50

            else -> 0.0
        }
        if (discount > 0) {
            usedCoupons.add(code)
            tinyDB.putListString("UsedCoupons", usedCoupons)

            Toast.makeText(this, "Coupon Applied", Toast.LENGTH_SHORT).show()
            calculateCart()
        } else {
            Toast.makeText(this, "Invalid Coupon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15.0

        tax = round((managementCart.getTotalFee() * percentTax) * 100) / 100.0

        total = round((managementCart.getTotalFee() + tax + delivery - discount) * 100) / 100.0
        val itemTotal = round(managementCart.getTotalFee() * 100) / 100.0

        binding.totalFeeTxt.text = "TK-$itemTotal"
        binding.taxTxt.text = "TK-$tax"
        binding.delivaryTxt.text = "TK-$delivery"
        binding.totalTxt.text = "TK-$total"
    }
}