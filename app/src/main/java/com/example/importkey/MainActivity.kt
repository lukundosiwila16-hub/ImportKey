package com.example.importkey

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.importkey.databinding.ActivityMainBinding
import com.example.importkey.databinding.ItemPhoneBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // How many of each phone the buyer wants (one number per phone, all start at 0)
    private val quantities = IntArray(phones.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Make one card for every phone, each with a - and + button
        for ((index, phone) in phones.withIndex()) {
            val card = ItemPhoneBinding.inflate(layoutInflater, binding.phoneContainer, false)
            card.tvPhoneName.text = phone.name
            card.tvPhonePrice.text = "US$" + "%,d".format(phone.price)
            card.tvQty.text = "0"

            card.btnPlus.setOnClickListener {
                if (quantities[index] < 9) {
                    quantities[index]++
                    card.tvQty.text = quantities[index].toString()
                    updateContinueButton()
                }
            }

            card.btnMinus.setOnClickListener {
                if (quantities[index] > 0) {
                    quantities[index]--
                    card.tvQty.text = quantities[index].toString()
                    updateContinueButton()
                }
            }

            binding.phoneContainer.addView(card.root)
        }

        updateContinueButton()

        // Continue: open the forwarder screen and send the quantities along
        binding.btnContinue.setOnClickListener {
            val next = Intent(this, ForwardersActivity::class.java)
            next.putExtra("quantities", quantities)
            startActivity(next)
        }
    }

    // The button is greyed out until at least one phone is chosen
    private fun updateContinueButton() {
        val count = quantities.sum()
        if (count == 0) {
            binding.btnContinue.text = "Choose at least one phone"
            binding.btnContinue.isEnabled = false
            binding.btnContinue.alpha = 0.4f
        } else {
            val word = if (count == 1) " item" else " items"
            binding.btnContinue.text = "Continue (" + count + word + ")"
            binding.btnContinue.isEnabled = true
            binding.btnContinue.alpha = 1f
        }
    }
}