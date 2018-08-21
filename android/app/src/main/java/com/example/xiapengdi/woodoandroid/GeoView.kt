package com.example.xiapengdi.woodoandroid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.xiapengdi.woodoandroid.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GeoView : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //Google map API documentation
    //Source: https://www.raywenderlich.com/230-introduction-to-google-maps-api-for-android-with-kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_view)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney
        //hard code location information

        val center = LatLng(30.28, -97.75)
//        val listofloc = new ArrayList<Posts>
//        pass a list of location for the marker, use the for loop to add location
        mMap.addMarker(MarkerOptions().position(center).title("Marker in Austin"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center))
    }

//    data class
}
