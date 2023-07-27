package com.mohamednader.shoponthego.Maps

interface MapResult {
    fun onMapResult(latitude: Double,
                    longitude: Double,
                    address: String,
                    country: String,
                    city: String,
                    street: String)
}