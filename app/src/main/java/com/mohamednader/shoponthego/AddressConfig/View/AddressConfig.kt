package com.mohamednader.shoponthego.AddressConfig.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mohamednader.shoponthego.Maps.MapResult
import com.mohamednader.shoponthego.Maps.MapsActivity
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Utils.MapResultListenerHolder
import com.mohamednader.shoponthego.databinding.ActivityAddressConfigBinding

class AddressConfig : AppCompatActivity(), MapResult {

    lateinit var binding: ActivityAddressConfigBinding
    val LOCATION_PICK: Int = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    fun initViews() {
        binding.mapBtn.setOnClickListener {
            MapResultListenerHolder.listener = this@AddressConfig as MapResult
            val resMapView = Intent(this, MapsActivity::class.java)
            startActivityForResult(resMapView, LOCATION_PICK)
        }

        binding.saveAddressBtn.setOnClickListener {
            if (validation()) {
                val fName = binding.firstNameEditText.text.toString()
                val sName = binding.secondNameEditText.text.toString()
                val address1 = binding.streetEditText.text.toString()
                val city = binding.cityEditText.text.toString()
                val county = binding.countryEditText.text.toString()
                val phone = binding.phoneEditText.text.toString()
                val address = Address(firstName = fName,
                        lastName = sName,
                        address1 = address1,
                        city = city,
                        country = county,
                        phone = phone)

                val resultIntent = Intent()
                resultIntent.putExtra("Address", address)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()

            }
        }
    }

    fun validation(): Boolean {
        if (binding.firstNameEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, First Name", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (binding.secondNameEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, Second Name", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (binding.streetEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, Street", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.cityEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, City", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.countryEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, Country", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.phoneEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, Phone Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    override fun onMapResult(latitude: Double,
                             longitude: Double,
                             address: String,
                             country: String,
                             city: String,
                             street: String) {

        Toast.makeText(this, "$address", Toast.LENGTH_SHORT).show()
        binding.streetEditText.setText(street)
        binding.cityEditText.setText(city)
        binding.countryEditText.setText(country)

    }
}