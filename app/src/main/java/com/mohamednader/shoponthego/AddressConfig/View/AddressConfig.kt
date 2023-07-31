package com.mohamednader.shoponthego.AddressConfig.View

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.mohamednader.shoponthego.Maps.MapResult
import com.mohamednader.shoponthego.Maps.MapsActivity
import com.mohamednader.shoponthego.Model.Pojo.Customers.Address
import com.mohamednader.shoponthego.Utils.MapResultListenerHolder
import com.mohamednader.shoponthego.databinding.ActivityAddressConfigBinding

class AddressConfig : AppCompatActivity(), MapResult {

    lateinit var binding: ActivityAddressConfigBinding
    val LOCATION_PICK: Int = 11
    private val REQUEST_LOCATION_PERMISSION_ID = 1001
    lateinit var countries: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    fun initViews() {
        binding.mapBtn.setOnClickListener {

            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    MapResultListenerHolder.listener = this@AddressConfig
                    val resMapView = Intent(this, MapsActivity::class.java)
                    startActivityForResult(resMapView, LOCATION_PICK)
                } else {
                    Toast.makeText(this@AddressConfig, "Turn On Location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions(
                        arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ), REQUEST_LOCATION_PERMISSION_ID
                )

            }




        }

        countries = listOf("Select Your Country",
                "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia",
                "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
                "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
                "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia",
                "Comoros", "Congo", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
                "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece",
                "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia",
                "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North",
                "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
                "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania",
                "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia",
                "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan",
                "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia",
                "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
                "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
                "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan",
                "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda",
                "Ukraine", "United Arab Emirates", "United Kingdom", "United States of America", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City",
                "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
        )


        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countrySpinner.adapter = adapter




        binding.saveAddressBtn.setOnClickListener {
            if (validation()) {
                val fName = binding.firstNameEditText.text.toString()
                val sName = binding.secondNameEditText.text.toString()
                val address1 = binding.streetEditText.text.toString()
                val city = binding.cityEditText.text.toString()
                val county = binding.countrySpinner.selectedItem.toString()
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
        if (binding.countrySpinner.selectedItem as String == "Select Your Country") {
            Toast.makeText(this@AddressConfig, "Please Select, Country", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.phoneEditText.text.isEmpty()) {
            Toast.makeText(this@AddressConfig, "Please Enter, Phone Number", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("INFO_TAG", "onRequestPermissionsResult: Permission granted")

            } else {
                Log.i("INFO_TAG", "onRequestPermissionsResult: Permission denied")
                Toast.makeText(
                        this@AddressConfig,
                        "Location permission denied. Unable to retrieve location.",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
                this@AddressConfig, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this@AddressConfig, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
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

        val selectedPosition = countries.indexOfFirst { it == country } ?: 0

        binding.countrySpinner.setSelection(selectedPosition)

    }
}