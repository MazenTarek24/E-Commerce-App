package com.mohamednader.shoponthego.Maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.Utils.MapResultListenerHolder
import com.mohamednader.shoponthego.databinding.ActivityMapsBinding
import java.io.IOException
import java.io.Serializable
import java.util.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback, Serializable,
                     ConfirmAddress.getDataDialogListener {

    private lateinit var binding: ActivityMapsBinding
    val TAG = "MapsActivity_INFO_TAG"

    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    var DEFALT_ZOOM = 15f
    private lateinit var mMap: GoogleMap
    var deviceLat = 0.0
    var deviceLon = 0.0
    private val REQUEST_LOCATION_PERMISSION_ID = 1001
    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        statusBarColor()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Location Section
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getDeviceLocation()
            } else {
                Toast.makeText(this@MapsActivity, "Turn On Location", Toast.LENGTH_LONG).show()
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



        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener { latLng ->
            val mMarkerData = MarkerData(
                    latLng.latitude, latLng.longitude, "Me", getAddress(latLng), R.drawable.pin
            )
            mMap.clear()
            createMarker(
                    mMarkerData.latitude,
                    mMarkerData.longitude,
                    mMarkerData.title,
                    mMarkerData.snippet,
                    mMarkerData.iconResId
            )
            Log.e(TAG, "This is the location of the res")
        }
    }

    private fun statusBarColor() {
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.main_color)
        }
    }

    fun getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            val location: Task<*> = mFusedLocationProviderClient.lastLocation
            location.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("Success", "done getting location in map ")
                    val currentLocation = task.result as Location
                    deviceLat = currentLocation.latitude
                    deviceLon = currentLocation.longitude
                    moveCamera(
                            LatLng(currentLocation.latitude, currentLocation.longitude), DEFALT_ZOOM
                    )
                } else {
                    val message = task.exception.toString()
                    Log.e("Error:", message)
                }

            }

        } catch (e: SecurityException) {
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    protected fun createMarker(
            latitude: Double, longitude: Double, title: String, snippet: String, iconID: Int
    ): GoogleMap? {
        mMap.addMarker(
                MarkerOptions().position(LatLng(latitude, longitude)).title(title).snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(iconID))
        )
        moveCamera(
                LatLng(latitude, longitude), DEFALT_ZOOM
        )
        return mMap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            val editTextString = data!!.getStringExtra("ADD")
            Log.e("MAPSDATA", "onActivityResult: $editTextString")
        }
    }

    private fun getAddress(latLng: LatLng): String {
        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        return try {
            addresses =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) as List<Address>
            val address = addresses[0].countryName + "/" + addresses[0].adminArea
            val city = addresses[0].locality
            val country = addresses[0].countryName
            val street = addresses[0].adminArea

            val ft = supportFragmentManager.beginTransaction()
            val prev = fragmentManager.findFragmentByTag("dialog")
            if (prev == null) {
                ft.addToBackStack(null)
                val dialogFragment = ConfirmAddress()
                val args = Bundle()
                args.putDouble("lat", latLng.latitude)
                args.putDouble("long", latLng.longitude)
                args.putString("address", address)
                args.putString("city", city)
                args.putString("country", country)
                args.putString("street", street)
                dialogFragment.arguments = args
                dialogFragment.show(ft, "dialog")
            }
            address
        } catch (e: IOException) {
            e.printStackTrace()
            "No Address Found"
        }
    }

    override fun onFinishDialog(lat: Double,
                                lon: Double,
                                Address: String,
                                country: String,
                                city: String,
                                street: String) {
        bundle.putDouble("selectedLat", lat)
        bundle.putDouble("selectedLong", lon)
        bundle.putString("selectedAddress", Address)
        Log.i(TAG, "onFinishDialog: FROM MAPS ACTIVITY : $lat, $lon, $Address ")


        MapResultListenerHolder.listener?.onMapResult(lat, lon, Address, country, city, street)
        finish()
//        mapResultDialog.setArguments(bundle)
//        mapResultDialog.show((this as FragmentActivity).supportFragmentManager, "MapResultDialog")
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: Permission granted")
                getDeviceLocation()
            } else {
                Log.i(TAG, "onRequestPermissionsResult: Permission denied")
                Toast.makeText(
                        this@MapsActivity,
                        "Location permission denied. Unable to retrieve location.",
                        Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
                this@MapsActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this@MapsActivity, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }

}
