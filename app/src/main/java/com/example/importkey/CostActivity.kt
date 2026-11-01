package com.example.importkey

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.importkey.databinding.ActivityCostBinding

class CostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCostBinding

    // Placeholder numbers. Change them here and everything updates.
    private val shippingPerItem = 40.0   // US$ international shipping for each item
    private val dutyRate = 0.10          // 10% customs duty ESTIMATE, check the real rate with ZRA
    private val vatRate = 0.16           // 16% import VAT
    private val kwachaPerDollar = 19.6   // exchange rate used in the deck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        binding = ActivityCostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Back arrow
        binding.btnBack.setOnClickListener { finish() }

        // Receive everything chosen on the first two screens
        val quantities = intent.getIntArrayExtra("quantities") ?: IntArray(phones.size)
        val forwarderName = intent.getStringExtra("forwarderName")
        val forwarderFee = intent.getIntExtra("forwarderFee", 0).toDouble()
        val verified = intent.getBooleanExtra("forwarderVerified", false)

        // Go through the phones, and add up every one that was chosen
        var subtotal = 0.0
        var itemCount = 0
        val lines = StringBuilder()
        for ((i, phone) in phones.withIndex()) {
            val q = quantities.getOrElse(i) { 0 }
            if (q > 0) {
                val lineTotal = phone.price.toDouble() * q
                subtotal += lineTotal
                itemCount += q
                lines.append(phone.name + " × " + q + " — " + money(lineTotal) + "\n")
            }
        }

        // The maths
        val shipping = shippingPerItem * itemCount        // shipping for every item
        val cif = subtotal + shipping                     // items + shipping
        val duty = cif * dutyRate                         // duty on the CIF value
        val vat = (cif + duty) * vatRate                  // VAT on CIF + duty
        val total = subtotal + shipping + forwarderFee + duty + vat

        // Show it
        binding.tvItems.text = lines.toString().trim()
        binding.tvForwarder.text = "via " + forwarderName
        if (!verified) {
            binding.tvWarning.visibility = View.VISIBLE
        }

        binding.tvItem.text = money(subtotal)
        binding.tvShipping.text = money(shipping)
        binding.tvFee.text = money(forwarderFee)
        binding.tvDuty.text = money(duty)
        binding.tvVat.text = money(vat)
        binding.tvTotal.text = money(total)
        binding.tvTotalZmw.text = "about ZMW " + "%,.0f".format(total * kwachaPerDollar)

        // Place order: show the message and lock the button
        binding.btnPlaceOrder.setOnClickListener {
            binding.tvStatus.visibility = View.VISIBLE
            binding.btnPlaceOrder.text = "Order placed"
            binding.btnPlaceOrder.isEnabled = false
        }
    }

    // Turns 1234.5 into US$1,234.50
    private fun money(amount: Double): String {
        return "US$" + "%,.2f".format(amount)
    }
}