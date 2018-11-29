package com.poly

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

//Curent address..........
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {



    private lateinit var mMap: GoogleMap
    private lateinit var instance: GetLocation
    var ischeck = false

    private  var arrayPoints: MutableList<LatLng> = mutableListOf()
    private  var list: MutableList<Model> = mutableListOf()
    private lateinit var polylineOptions: PolylineOptions

    private var checkClick = false

    /* recevie Broadcast lattude and longtude*/
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent != null && !ischeck) {
                val lat = intent.getDoubleExtra("lat", 0.0)
                val lng = intent.getDoubleExtra("lng", 0.0)
                val address = intent.getStringExtra("add")
                val sydney = LatLng(lat, lng)

               // mapMaker(address, sydney, R.drawable.pick_location)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        list.add(Model(LatLng(12.931335, 77.678277),"Delhi"))
        list.add(Model(LatLng(12.930499, 77.678578),"Mumbai"))
        list.add(Model(LatLng(12.931963, 77.679307),"Faridabad"))
        list.add(Model(LatLng(12.932297, 77.677548),"odisa"))
        list.add(Model(LatLng(12.931795, 77.676110),"Kerla"))
        list.add(Model(LatLng(12.930624, 77.679651),"bag"))
        list.add(Model(LatLng(12.931335, 77.679779),"bag"))
        list.add(Model(LatLng(12.928700, 77.680723),"Eco"))
        list.add(Model(LatLng(12.929244, 77.679887),"Eco"))
        list.add(Model(LatLng(12.931210, 77.680166),"Eco"))
        list.add(Model(LatLng(12.929829, 77.676861),"Eco"))
        list.add(Model(LatLng(12.930603, 77.676518),"Eco"))
        list.add(Model(LatLng(12.927633, 77.677269),"Eco"))

        initBroadCastMap()
        instance.startLocation()


    }

    private fun initBroadCastMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        instance = GetLocation(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("key_action"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            GetLocation.REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> Log.e("TAG", "User agreed to make required location settings changes.")
                    Activity.RESULT_CANCELED -> {
                        Log.e("TAG", "User chose not to make required location settings changes.")
                        instance.mRequestingLocationUpdates = false
                    }
                }// Nothing to do. startLocationupdates() gets called in onResume again.
            }
        }
    }


    public override fun onResume() {
        super.onResume()
        if (instance.mRequestingLocationUpdates!! && instance.checkPermissions()) {
            instance.startLocationUpdates()
        }
        instance.updateLocationUI()
    }

    override fun onPause() {
        super.onPause()
        if (instance.mRequestingLocationUpdates!!) {
            instance.stopLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.mMap = googleMap;
        val style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style);
        this.mMap.setMapStyle(style);
        mMap.isMyLocationEnabled = true




        mMap.setOnMapLoadedCallback {
            // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30))
            run()
        }


    }



    fun run(){

        val bulder=LatLngBounds.Builder()
       // bulder.include()
       // bulder.include()

        for(l in list){
            bulder.include(l.latlng)
            val markarOption=MarkerOptions().position(l.latlng).title(l.name).snippet(l.name)
            mMap.addMarker(markarOption)
        }

        val bound =bulder.build()
        val cameraUpdate=CameraUpdateFactory.newLatLngBounds(bound,50)
        mMap.moveCamera(cameraUpdate)

        val sydney = LatLng(12.931544,77.678835)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.7f), 200, null)


        val marker= MarkerOptions()
        marker.position(sydney);
        mMap.addMarker(marker);
        val  circle= CircleOptions()
        circle.center(sydney)
                .fillColor(Color.TRANSPARENT)
                .radius(500.0)
                .strokeColor(Color.BLUE)
                .strokeWidth(2f);
        mMap.addCircle(circle);
    }
}
