package com.example.mapmove

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.dynamic.IFragmentWrapper

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val ACCESS_LOCATION_REQUEST = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

            checkPermisson()


    }


    private fun checkPermisson(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
                //Permisson request
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESS_LOCATION_REQUEST)
              return
            }
        }
        getLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,3f,myLocation)

        val myThead = MyThread()
        myThead.start()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        // Add a marker in Sydney and move the camera


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESS_LOCATION_REQUEST ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    getLocation()
                }else{
                    Toast.makeText(applicationContext, "This permisson is required!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    var location:Location? = null
    inner class MyLocationListener:LocationListener{

        //construktor
        constructor(){
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(loc: Location) {
         location = loc
            Log.d("Tag", "Loc:$loc")
            Log.d("Tag", "Loc:$location")
        }

        override fun onProviderDisabled(provider: String) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
          //GPS ni ochib yonishi

        }

    }

    inner class MyThread: Thread {
        constructor() : super(){

        }

        override fun run() {
            while(true){
              try {
                  runOnUiThread {
                      val uychi = LatLng(location!!.latitude, location!!.longitude)
                      mMap.clear()
                      mMap.addMarker(
                          MarkerOptions()
                              .position(uychi)
                              .title("Marker in Uychi")
                              .snippet("This is my Location")
                              //.icon(BitmapDescriptorFactory.fromResource(R.drawable.mario3))
                      )
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uychi, 15f))
                  }
                  sleep(1000)
              }catch (ex:Exception){ex.printStackTrace()}
            }
        }
    }
}



