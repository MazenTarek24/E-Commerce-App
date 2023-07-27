package com.mohamednader.shoponthego.Maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.FragmentConfirmAddressDialogBinding

class ConfirmAddress : DialogFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentConfirmAddressDialogBinding
    private val TAG = "ConfirmAddress_INFO_TAG"

    private val DEFALT_ZOOM = 15f
    private lateinit var listener: getDataDialogListener
    private lateinit var mMap: GoogleMap
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private lateinit var address: String
    private lateinit var city: String
    private lateinit var country: String
    private lateinit var street: String
    private var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lat = requireArguments().getDouble("lat")
        lon = requireArguments().getDouble("long")
        address = requireArguments().getString("address") ?: ""
        city = requireArguments().getString("street") ?: ""
        country = requireArguments().getString("country") ?: ""
        street = requireArguments().getString("street") ?: ""
        Log.i(TAG, "onCreate: $lat , and $lon , and $address")

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmAddressDialogBinding.inflate(inflater, container, false)
        binding.myAddressText.text = address
        val fm = childFragmentManager

        mapFragment = fm.findFragmentById(R.id.mapp) as? SupportMapFragment

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapp, mapFragment!!).commit();
        }

        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectBtn.setOnClickListener {
            dismiss()
            listener.onFinishDialog(lat, lon, address, country, city, street)
        }

        binding.changeBtn.setOnClickListener {
            dismiss()
        }

        dialog?.setCanceledOnTouchOutside(true)

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        mapFragment?.let { requireFragmentManager().beginTransaction().remove(it).commit() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismiss()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = false
        val markerData = MarkerData(lat, lon, "Me", address, R.drawable.pin)
        mMap.clear()
        createMarker(
                markerData.latitude,
                markerData.longitude,
                markerData.title,
                markerData.snippet,
                markerData.iconResId
        )
        Log.i(TAG, "This is the location of the res")
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun createMarker(
            latitude: Double, longitude: Double, title: String, snippet: String, iconID: Int
    ) {
        mMap.addMarker(
                MarkerOptions().position(LatLng(latitude, longitude)).title(title).snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(iconID))
        )
        moveCamera(
                LatLng(latitude, longitude), DEFALT_ZOOM
        )
    }

    interface getDataDialogListener {
        fun onFinishDialog(lat: Double,
                           lon: Double,
                           address: String,
                           country: String,
                           city: String,
                           street: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as getDataDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                    "$context must implement getDataDialogListener"
            )
        }
    }
}
