package com.example.importkey

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.importkey.databinding.ActivityForwardersBinding
import com.example.importkey.databinding.ItemForwarderBinding

class ForwardersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForwardersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        binding = ActivityForwardersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Back arrow
        binding.btnBack.setOnClickListener { finish() }

        // Receive how many of each phone was chosen on the first screen
        val quantities = intent.getIntArrayExtra("quantities") ?: IntArray(phones.size)

        // Work out how many items and the subtotal
        var itemCount = 0
        var subtotal = 0
        for ((i, phone) in phones.withIndex()) {
            val q = quantities.getOrElse(i) { 0 }
            itemCount += q
            subtotal += phone.price * q
        }
        val word = if (itemCount == 1) " item" else " items"
        binding.tvSelectedPhone.text = itemCount.toString() + word + " - US$" + "%,d".format(subtotal)

        // Make one card for every forwarder in the list
        for (f in forwarders) {
            val card = ItemForwarderBinding.inflate(layoutInflater, binding.forwarderContainer, false)

            card.tvForwarderName.text = f.name
            card.tvCity.text = f.city
            card.tvRating.text = "★ " + f.rating + " · " + f.deliveries + " deliveries"
            card.tvStores.text = "Stores: " + f.stores
            card.tvDays.text = "Delivery: " + f.days
            card.tvFee.text = "Fee: US$" + f.fee

            // Green tick for verified forwarders, grey text for the rest
            if (f.verified) {
                card.tvBadge.text = "✓ ID verified"
                card.tvBadge.setTextColor(ContextCompat.getColor(this, R.color.green))
            } else {
                card.tvBadge.text = "Not verified"
                card.tvBadge.setTextColor(ContextCompat.getColor(this, R.color.text_muted))
            }

            card.root.setOnClickListener {
                // Open the checkout screen and send it everything chosen so far
                val next = Intent(this, CostActivity::class.java)
                next.putExtra("quantities", quantities)
                next.putExtra("forwarderName", f.name)
                next.putExtra("forwarderFee", f.fee)
                next.putExtra("forwarderVerified", f.verified)
                startActivity(next)
            }

            binding.forwarderContainer.addView(card.root)
        }
    }
}