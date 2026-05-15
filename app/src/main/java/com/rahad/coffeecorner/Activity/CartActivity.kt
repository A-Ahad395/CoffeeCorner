package com.rahad.coffeecorner.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rahad.coffeecorner.Adapter.CartAdapter
import com.rahad.coffeecorner.Domain.ChangeNumberItemsListener
import com.rahad.coffeecorner.Domain.OrderModel
import com.rahad.coffeecorner.Helper.ManagmentCart
import com.rahad.coffeecorner.databinding.ActivityCartBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

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
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )

        binding.listView.adapter =
            CartAdapter(
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

            val orderList =
                managementCart.getListCart()

            if (orderList.isEmpty()) {

                Toast.makeText(
                    this,
                    "Cart is empty",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val currentDate =
                SimpleDateFormat(
                    "dd MMM yyyy hh:mm a",
                    Locale.getDefault()
                ).format(Date())

            val order =
                OrderModel(
                    orderDate = currentDate,
                    totalCost = total,
                    items = orderList
                )

            saveOrderToFirebase(order)
        }

        binding.applyBtn.setOnClickListener {

            applyCoupon(
                binding.editTextText2.text
                    .toString()
                    .trim()
            )
        }
    }

    private fun saveOrderToFirebase(order: OrderModel) {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid

        if (uid == null) {

            Toast.makeText(
                this,
                "User not logged in",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(uid)
            .child("Orders")
            .push()
            .setValue(order)

            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Order Confirmed",
                    Toast.LENGTH_SHORT
                ).show()

                managementCart.clearCart()

                startActivity(
                    Intent(
                        this,
                        MyOrderActivity::class.java
                    )
                )

                finish()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Order failed: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun applyCoupon(couponCode: String) {

        val tinyDB =
            com.rahad.coffeecorner.Helper.TinyDB(this)

        val code =
            couponCode.uppercase()

        if (code.isEmpty()) {

            Toast.makeText(
                this,
                "Enter coupon code",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val usedCoupons =
            tinyDB.getListString("UsedCoupons")

        if (usedCoupons.contains(code)) {

            Toast.makeText(
                this,
                "Coupon already used",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val subTotal =
            managementCart.getTotalFee()

        discount =
            when (code) {

                "C10" -> subTotal * 0.10
                "C15" -> subTotal * 0.15
                "C20" -> subTotal * 0.20
                "C25" -> subTotal * 0.25
                "C30" -> subTotal * 0.30
                "C35" -> subTotal * 0.35
                "C40" -> subTotal * 0.40
                "C11" -> subTotal * 0.11
                "C12" -> subTotal * 0.12
                "C13" -> subTotal * 0.13

                else -> 0.0
            }

        if (discount > 0) {

            usedCoupons.add(code)

            tinyDB.putListString(
                "UsedCoupons",
                usedCoupons
            )

            Toast.makeText(
                this,
                "Coupon Applied",
                Toast.LENGTH_SHORT
            ).show()

            calculateCart()

        } else {

            Toast.makeText(
                this,
                "Invalid Coupon",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun calculateCart() {

        val cartList =
            managementCart.getListCart()

        val percentTax = 0.02
        val delivery = 70.0

        if (cartList.isEmpty()) {

            tax = 0.0
            total = 0.0
            discount = 0.0

            binding.totalFeeTxt.text = "TK-0.0"
            binding.taxTxt.text = "TK-0.0"
            binding.delivaryTxt.text = "TK-0.0"
            binding.totalTxt.text = "TK-0.0"

            return
        }

        val itemTotal =
            round(
                managementCart.getTotalFee() * 100
            ) / 100.0

        if (discount > itemTotal) {
            discount = itemTotal
        }

        tax =
            round(
                (itemTotal * percentTax) * 100
            ) / 100.0

        total =
            round(
                (
                        itemTotal +
                                tax +
                                delivery -
                                discount
                        ) * 100
            ) / 100.0

        binding.totalFeeTxt.text =
            "TK-$itemTotal"

        binding.taxTxt.text =
            "TK-$tax"

        binding.delivaryTxt.text =
            "TK-$delivery"

        binding.totalTxt.text =
            "TK-$total"
    }
}