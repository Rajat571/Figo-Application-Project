package com.pearl.figgodriver

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.pearl.PrefManager
import com.pearl.figgodriver.databinding.ActivityCityRideBinding
import com.pearl.pearllib.BaseClass

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class CityRideActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding:ActivityCityRideBinding
    lateinit var prefManager: PrefManager
    var lat:Double = 0.0
    var long:Double = 0.0
    var current_lat:Double=0.0
    var current_long:Double=0.0
    var des_lat:Double=0.0
    var des_long:Double=0.0
    var ride_id:Int=0
    var ride_request_id:Int=0
    var customer_booking_id:String=""
    private var originLatitude: Double =30.28401063526107
    private var originLongitude: Double = 77.99210085398012
    private var destinationLatitude: Double =  30.35335500972683
    private var destinationLongitude: Double = 78.02461312748794
    private lateinit var mMap: GoogleMap
    lateinit var baseClass: BaseClass
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_city_ride)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
/*
        var address_to= intent.getStringExtra("location_to")
        var date=intent.getStringExtra("customer_date")
        var time=intent.getStringExtra("customer_time")
         customer_booking_id=intent.getStringExtra("customer_booking_id").toString()
        current_lat=intent.getStringExtra("current_lat")!!.toDouble()
        current_long=intent.getStringExtra("current_long")!!.toDouble()
        des_lat=intent.getStringExtra("des_lat")!!.toDouble()
        des_long=intent.getStringExtra("des_long")!!.toDouble()
         ride_id=intent.getStringExtra("ride_id")!!.toInt()
        ride_request_id=intent.getStringExtra("ride_request_id")!!.toInt()
        Log.d("CityRideActivity","ride_id====="+ride_request_id)
        binding.cityRideAddress.text=address_to
        binding.cityRideDate.text=date
        binding.cityRideTime.text=time*/

       // Log.d("SEND DATA","LOCATION_DATA====="+address_to.toString()+"\n"+current_lat.toString()+"\n"+current_long+"\n"+customer_booking_id+"\n"+ride_id+"\n"+ride_request_id)

        baseClass=object :BaseClass(){
            override fun setLayoutXml() {

            }

            override fun initializeViews() {
                TODO("Not yet implemented")
            }

            override fun initializeClickListners() {
            }

            override fun initializeInputs() {
                TODO("Not yet implemented")
            }

            override fun initializeLabels() {
                TODO("Not yet implemented")
            }
        }
        initializeClickListners()

        prefManager=PrefManager(this)

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
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    lat = location.latitude
                    prefManager.setlatitude(lat.toFloat())
                    long = location.longitude
                    prefManager.setlongitude(long.toFloat())
                    Toast.makeText(this,"Lat :"+lat+"\nLong: "+long, Toast.LENGTH_SHORT).show()
                }
            }
        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = applicationContext.packageManager
            ?.getApplicationInfo(applicationContext.applicationContext!!.packageName, PackageManager.GET_META_DATA)!!

        val value = ai.metaData["com.google.android.geo.${R.string.Google_maps_key}"]
        val apiKey = value.toString()
        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext.applicationContext,apiKey)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.RTXAmap) as SupportMapFragment
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
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
        }
    }
    private fun initializeClickListners() {
        binding.rejectCityRideBtn.setOnClickListener {
          /*  var url="https://test.pearl-developer.com/figo/api/driver-ride/reject-city-ride-request"
            var queue=Volley.newRequestQueue(this)
            var json=JSONObject()
            json.put("ride_request_id",ride_request_id)

            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url,json,
                    com.android.volley.Response.Listener<JSONObject?> { response ->
                        Log.d("CityRideActivity", "Reject status response===" + response)
                        *//*  var statuss=response.getString("status")
                          if (statuss.equals(true)) {*//*
                        var message=response.getString("message")
                        Toast.makeText(this@CityRideActivity, "rejected"+message, Toast.LENGTH_LONG).show()
                        finish()
                        //}
                        // Get your json response and convert it to whatever you want.
                    }, object : com.android.volley.Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            Log.d("CityRideActivity", "error===" + error)
                            Toast.makeText(this@CityRideActivity, "Something went wrong!", Toast.LENGTH_LONG).show()
                        }
                    })

                {
                    @SuppressLint("SuspiciousIndentation")
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers: MutableMap<String, String> = HashMap()
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        headers.put("Authorization", "Bearer " + prefManager.getToken());
                        return headers
                    }
                }

            queue.add(jsonOblect)*/
            // Toast.makeText(this,"Accepted",Toast.LENGTH_SHORT).show()


            // Toast.makeText(this," This Booking is rejected",Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.acceptCityRideBtn.setOnClickListener {
            prefManager.setActiveRide(1)
             startActivity(Intent(this,DriverDashBoard::class.java))
/*
            var url="https://test.pearl-developer.com/figo/api/driver-ride/accept-city-ride-request"
            var queue=Volley.newRequestQueue(this)
            var json=JSONObject()
            json.put("ride_id",ride_id)

            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url,json,
                    com.android.volley.Response.Listener<JSONObject?> { response ->
                        Log.d("CityRideActivity", "Accept status response===" + response)

                            var message=response.getString( "message")
                            Toast.makeText(this@CityRideActivity, ""+message, Toast.LENGTH_LONG).show()

                        var data=response.getJSONObject( "data")
                        var ride=response.getJSONObject("data").getJSONObject("ride")
                        var id=ride.getString("id")
                        Log.d("CityRideActivity", "id===" + id)

                        var to_location=response.getJSONObject("data").getJSONObject( "ride").getJSONObject( "to_location")
                        var to_location_lat=to_location.getString("lat")
                        var to_location_long=to_location.getString("lng")
                        var address_name=to_location.getString("name")
                        Log.d("SendData", "to_location" + to_location_lat+"\n"+to_location_long+"\n"+address_name)

                        var from_location=response.getJSONObject("data").getJSONObject( "ride").getJSONObject( "from_location")
                        var from_location_lat=from_location.getString("lat")
                        var from_location_long=from_location.getString("lng")
                        var from_name=from_location.getString("name")
                        Log.d("SendData", "to_location" + from_location_lat+"\n"+from_location_long+"\n"+from_name)

                        var date_only=ride.getString("date_only")
                        var time_only=ride.getString( "time_only")
                        var customer=response.getJSONObject("data").getJSONObject("customer")
                        var customer_id=customer.getString("id")
                        var customer_name=customer.getString( "name")
                        var customer_contact=customer.getString( "contact_no")



                            //prefManager.setActiveRide(1)
                           // startActivity(Intent(this,DriverDashBoard::class.java))


                    }, object : com.android.volley.Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            Log.d("CityRideActivity", "error===" + error)
                            Toast.makeText(this@CityRideActivity, "Something went wrong!", Toast.LENGTH_LONG).show()
                        }
                    })
                {
                    @SuppressLint("SuspiciousIndentation")
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers: MutableMap<String, String> = HashMap()





                        
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        headers.put("Authorization", "Bearer " + prefManager.getToken());
                        return headers
                    }
                }
            queue.add(jsonOblect)*/
           // Toast.makeText(this,"Accepted",Toast.LENGTH_SHORT).show()l
        }
    }




    override fun onMapReady(googleMap: GoogleMap) {
        mMap =googleMap
       /* val originLocation = LatLng(originLatitude, originLongitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))*/
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
                val respObj = Gson().fromJson(data,MapData::class.java)
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

    override fun onDestroy() {
        super.onDestroy()
    }


}