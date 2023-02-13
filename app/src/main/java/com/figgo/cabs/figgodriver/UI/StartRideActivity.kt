package com.figgo.cabs.figgodriver.UI

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.ActivityStartRideBinding
import com.figgo.cabs.figgodriver.Location
import com.figgo.cabs.figgodriver.MapData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class StartRideActivity : AppCompatActivity(), OnMapReadyCallback {

    private var originLatitude: Double =30.28401063526107
    private var originLongitude: Double = 77.99210085398012
    private var destinationLatitude: Double =  30.35335500972683
    private var destinationLongitude: Double = 78.02461312748794
    private lateinit var driverlocation:LatLng
    private lateinit var customerLocation:LatLng
    private var customerLAT:Double=0.0
    private var customerLON:Double=0.0
    private lateinit var dropLocation:LatLng
    private lateinit var mMap: GoogleMap
    lateinit var binding: ActivityStartRideBinding
     var rideID:Int = 0
    private var timer:CountDownTimer?=null
    lateinit var prefManager: PrefManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var rideId:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this, R.layout.activity_start_ride)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
prefManager= PrefManager(this)
        rideId = prefManager.getRideID()
        var arrow_up_btn=findViewById<ImageView>(R.id.arrow_up_IV)
        binding.arrowDownIV.setOnClickListener {
            TransitionManager.beginDelayedTransition( binding.startRideBottomLayout, AutoTransition())
            binding.startRideBottomLayout.visibility=View.VISIBLE

        }
        arrow_up_btn.setOnClickListener {
            TransitionManager.beginDelayedTransition( binding.startRideBottomLayout, AutoTransition())
            var start_ride_bottom_layout=findViewById<LinearLayout>(R.id.start_ride_bottom_layout)
            start_ride_bottom_layout.visibility=View.GONE
        }

        val ai: ApplicationInfo = applicationContext.packageManager
            ?.getApplicationInfo(applicationContext.applicationContext!!.packageName, PackageManager.GET_META_DATA)!!
        val value = ai.metaData["com.google.android.geo.${R.string.Google_maps_key}"]
        val apiKey = value.toString()
        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext.applicationContext,apiKey)
        }

        val database = FirebaseDatabase.getInstance()
        val customerRef = database.getReference("$rideId customer")
        val driverRef = database.getReference("$rideId driver")

var count:Int = 0
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.getMapAsync {
            mMap = it
            // val originLocation = LatLng(current_lat!!, current_long!!)
            val originLocation = LatLng( originLatitude, originLongitude)
            mMap.addMarker(MarkerOptions().position(originLocation).title("hey"))
            // val destinationLocation = LatLng(des_lat!!, des_long!!)
            val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            mMap.addMarker(MarkerOptions().position(destinationLocation).title("hi"))
            val urll = getDirectionURL(originLocation, destinationLocation, "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
            //GetDirection(urll).execute()

            timer = object: CountDownTimer(100000, 500) {
                override fun onTick(millisUntilFinished: Long) {
                    val driverData = driverRef.child("loc $count")
                    count++
                    customerLAT=0.0
                    customerLON=0.0
                    //setCurrentLatLon()
                    originLatitude+=0.0001
                    originLongitude+=0.0001
                    driverData.child("LAT ").setValue("$originLatitude")
                    driverData.child("LON ").setValue("$originLongitude")

                    liveRouting(originLatitude,originLongitude,customerLAT,customerLON)
                }
                override fun onFinish() {

                }
            }
            (timer as CountDownTimer).start()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 11F))
            

        }
    }


    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()?.string()
            Log.d("SEND DATA"," data ===="+ data )

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.GREEN)
                lineoption.geodesic(true)
               /* lineoption.startCap()
                if (result.lastIndex.equals(LatLng(destinationLatitude, destinationLongitude))){

                }*/
            }
            mMap.addPolyline(lineoption)
        }
    }
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap =p0
    }

    fun liveRouting(driver_lat:Double,driver_lon:Double,cust_lat:Double,cust_lon:Double){

        mMap.clear()

        driverlocation   = LatLng(driver_lat, driver_lon)
        dropLocation  = LatLng(destinationLatitude, destinationLongitude)
        //customerLocation = LatLng(cust_lat)
        val height = 80
        val width = 80
        val bitmapdraw = resources.getDrawable(R.drawable.ic_drivercab) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        mMap.addMarker(
            MarkerOptions().position(driverlocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title("Current Location")
        )
        val bitmapdraw2 = resources.getDrawable(R.drawable.ic_destination) as BitmapDrawable
        val b2 = bitmapdraw2.bitmap
        val smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false)
        mMap.addMarker(
            MarkerOptions().position(dropLocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2))
                .title("drop-off")
        )

        val source = "$driver_lat,$driver_lon"
        val destination = "$destinationLatitude,$destinationLongitude"
        Log.e("Origin ", "$source\n Destination $destination")
        //GetDirection().execute(source, destination)
        var url:String=getDirectionURL(driverlocation!!, dropLocation!!,"AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
        GetDirection(url).execute()
        Handler().postDelayed({
            //do something
        }, 5000)
    }

    fun setCurrentLatLon(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: android.location.Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    originLatitude = location.latitude
                    prefManager.setlatitude(originLatitude.toFloat())
                    originLongitude = location.longitude
                    prefManager.setlongitude(originLongitude.toFloat())
                    //Toast.makeText(this,"Lat :"+lat+"\nLong: "+long, Toast.LENGTH_SHORT).show()
                }
            }
    }
}