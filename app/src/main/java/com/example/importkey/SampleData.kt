package com.example.importkey

// One phone = a name and a price in US dollars
data class Phone(val name: String, val price: Int)

// One forwarder. Fee is in US dollars
data class Forwarder(
    val name: String,
    val city: String,
    val rating: Double,
    val deliveries: Int,
    val stores: String,
    val days: String,
    val fee: Int,
    val verified: Boolean
)

// Sample phones. Only the iPhone price comes from the deck, the rest are placeholders
val phones = listOf(
    Phone("iPhone 18 Pro", 1199),
    Phone("Samsung Galaxy Ultra", 1299),
    Phone("Google Pixel Pro", 999),
    Phone("Budget Android phone", 299)
)

// Sample forwarders. Only Mutale S. comes from the deck, the rest are made up.
// The last one is unverified on purpose: cheapest, but risky, which is the problem we solve.
val forwarders = listOf(
    Forwarder("Mutale S.", "Dallas, USA", 4.9, 128, "Amazon, Best Buy, Apple", "10–14 days", 35, true),
    Forwarder("Chanda M.", "Houston, USA", 4.7, 86, "Amazon, Best Buy", "10–14 days", 30, true),
    Forwarder("Bwalya K.", "London, UK", 4.8, 64, "Amazon UK, Apple", "12–16 days", 28, true),
    Forwarder("WhatsApp agent", "Location unknown", 3.4, 12, "Any store", "Unknown", 15, false)
)