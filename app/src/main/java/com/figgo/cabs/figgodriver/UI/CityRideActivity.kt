package com.figgo.cabs.figgodriver.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.*
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.ActivityCityRideBinding
import com.figgo.cabs.figgodriver.LiveRouting
import com.figgo.cabs.figgodriver.MapData
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.Helper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*


class CityRideActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener
 {
    lateinit var binding: ActivityCityRideBinding
    lateinit var prefManager: PrefManager
    var lat:Double = 0.0
    var long:Double = 0.0
    var current_lat:Double=0.0
    var current_long:Double=0.0
    var des_lat:Double=0.0
    var des_long:Double=0.0
    var pickupLocation: LatLng? = null
    var dropLocation: LatLng? = null
     lateinit var defaultLayout:LinearLayout
     lateinit var acceptwaitLayout:LinearLayout
     private var timer:CountDownTimer?=null
     lateinit var price:String
    var ride_id:Int=0
     var accepted:Int=0
    var ride_request_id:Int=0
    var customer_booking_id:String=""
    private var originLatitude: Double =30.28401063526107
    private var originLongitude: Double = 77.99210085398012
    private var destinationLatitude: Double =  30.35335500972683
    private var destinationLongitude: Double = 78.02461312748794
    var testlat:Double=30.296748
    var testlon:Double = 77.976038
    private lateinit var mMap: GoogleMap
    lateinit var baseClass: BaseClass
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
     lateinit var timertv :TextView
    var ride_type=""

     var booking_id:String=""
     var type:String=""
     var to_location_lat:String=""
     var to_location_long:String=""
     var address_name:String=""
     var from_location_lat:String=""
     var from_location_long:String=""
     var from_name:String=""
     var date_only:String=""
     var time_only:String=""
     var customer_id:String=""
     var customer_name:String=""
     var customer_contact:String=""
     var outstation_id=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_city_ride)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        var liveRouting = LiveRouting()

        var address_to= intent.getStringExtra("location_to")
        var date=intent.getStringExtra("customer_date")
        var time=intent.getStringExtra("customer_time")
         customer_booking_id=intent.getStringExtra("customer_booking_id").toString()
        current_lat=intent.getStringExtra("current_lat")!!.toDouble()
        current_long=intent.getStringExtra("current_long")!!.toDouble()
        des_lat=intent.getStringExtra("des_lat")!!.toDouble()
        des_long=intent.getStringExtra("des_long")!!.toDouble()
         ride_id=intent.getStringExtra("ride_id")!!.toInt()
        ride_type=intent.getStringExtra("ride_type").toString()
        outstation_id=intent.getIntExtra("outstation_id",0)
        timertv = findViewById<TextView>(R.id.timer5min)

      //  liveRouting.firebaseInit(ride_id)
        ride_request_id=intent.getStringExtra("ride_request_id")!!.toInt()
        defaultLayout=findViewById(R.id.city_ride_defaultlayout)
        acceptwaitLayout=findViewById(R.id.cityride_wait_layout)
        Log.d("CityRideActivity","ride_id====="+ride_id)
        binding.locationTo.text=address_to
        binding.locationFrom.text=intent.getStringExtra("location_from")
        binding.cityRideDate.text=date
        binding.cityRideTime.text=time
        binding.bookingId.text=customer_booking_id
        binding.price.text=intent.getStringExtra("price").toString()
        price=intent.getStringExtra("price").toString()
        defaultLayout.visibility= View.VISIBLE
        acceptwaitLayout.visibility=View.GONE

       // Log.d("SEND DATA","LOCATION_DATA====="+address_to.toString()+"\n"+current_lat.toString()+"\n"+current_long+"\n"+customer_booking_id+"\n"+ride_id+"\n"+ride_request_id)

        baseClass=object : BaseClass(){
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

        prefManager= PrefManager(this)
        prefManager.setRideID(ride_id)

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
            /*    if (location == null)
                    //Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()*/
                    if (location != null) {
                    lat = location.latitude
                    prefManager.setlatitude(lat.toFloat())
                    long = location.longitude
                    prefManager.setlongitude(long.toFloat())
                    //Toast.makeText(this,"Lat :"+lat+"\nLong: "+long, Toast.LENGTH_SHORT).show()
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

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.getMapAsync {
            mMap = it
            val height = 80
            val width = 80
            val bitmapdraw = resources.getDrawable(R.drawable.ic_destination) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            val originLocation = LatLng(current_lat!!, current_long!!)
           // val originLocation = LatLng( originLatitude, originLongitude)
            mMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .position(originLocation).title("hey"))
            val destinationLocation = LatLng(des_lat!!, des_long!!)
            //val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            mMap.addMarker(MarkerOptions()
                .position(destinationLocation).title("hi"))
            val urll = getDirectionURL(originLocation, destinationLocation, "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
            GetDirection(urll).execute()
            var newlat=30.288415
            var new_lon=78.014850
           // liveRouting(newlat, new_lon)
         /*  timer = object: CountDownTimer(100000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    newlat+=0.001
                    new_lon+=0.001
                }
                override fun onFinish() {

                }
            }
            (timer as CountDownTimer).start()
*/

            //liveRouting(newlat, new_lon)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 12F))
        }

    }

    private fun initializeClickListners() {

        binding.rejectCityRideBtn.setOnClickListener {
            reject()
        }

        binding.acceptCityRideBtn.setOnClickListener {

            accepted=1
           // var url="https://test.pearl-developer.com/figo/api/driver-ride/accept-city-ride-request"

            defaultLayout.visibility= View.GONE
            acceptwaitLayout.visibility=View.VISIBLE

            var min=5
            var sec:Int=60
            timer = object: CountDownTimer(300000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if(sec==60) {
                        timertv.text = "0" + min + " : " + "00"
                        min-=1
                        sec -= 1
                    }
                    else if(sec<60&&sec>=0) {
                                  timertv.setText("0" + min + " : " + sec)
                        sec=sec-1
                    }
                    if(sec==0){
                        sec=60
                    }
                    if(millisUntilFinished.toInt()%5==0) {
                        Log.d("TIMER ",". . . . . .")
                        acceptwait()
                    }
                }
                override fun onFinish() {
                    //navigatenext()
                   // Toast.makeText(this,"Booking Failed",Toast.LENGTH_SHORT).show()
                    //reject()
                }

            }
            (timer as CountDownTimer).start()

         }
    }
     private fun acceptwait() {
         Log.d("RES CITY", "Enter Functions")
         //   Toast.makeText(this,"Accept API RUNIING",Toast.LENGTH_SHORT).show()
         var min = 5
         var sec: Int = 60

         var finished: Boolean = false

         if (ride_type == "advance") {
             var url=Helper.accept_advance_ride_request

             Log.d("CityRideActivity", "Advance Accept status URL===" + url)
             var queue = Volley.newRequestQueue(this)
             var json = JSONObject()
             json.put("ride_id", ride_id)
             Log.d("Ride id", "Ride id===" + json)


             val jsonOblect: JsonObjectRequest =
                 object : JsonObjectRequest(Method.POST, url, json,
                     com.android.volley.Response.Listener<JSONObject?> { response ->

                         if (response != null) {

                             var data = response.getJSONObject("data")

                             var ride = data.getJSONObject("ride")
                             var id = ride.getString("id")
                             var status = ride.getString("status")
                             booking_id = ride.getString("booking_id")
                             type = ride.getString("booking_type")

                             Log.d("RES CITY", "Advance Accept status response===" + response)


                             var to_location =
                                 response.getJSONObject("data").getJSONObject("ride")
                                     .getJSONObject("to_location")
                             to_location_lat = to_location.getString("lat")
                             to_location_long = to_location.getString("lng")
                             address_name = to_location.getString("name")
                             /*     Log.d(
                                             "SendData",
                                             "to_location" + to_location_lat + "\n" + to_location_long + "\n" + address_name
                                         )*/

                             var from_location = response.getJSONObject("data").getJSONObject("ride")
                                 .getJSONObject("from_location")
                             from_location_lat = from_location.getString("lat")
                             from_location_long = from_location.getString("lng")
                             from_name = from_location.getString("name")
                             /*       Log.d(
                                             "SendData",
                                             "to_location" + from_location_lat + "\n" + from_location_long + "\n" + from_name
                                         )*/

                             date_only = ride.getString("date_only")
                             time_only = ride.getString("time_only")
                             try {
                                 var customer =
                                     response.getJSONObject("data").getJSONObject("customer")

                                 if (customer.getString("id").equals("null"))
                                     customer_id = " - - - "
                                 else
                                     customer_id = customer.getString("id")

                                 if (customer.getString("name").equals("null"))
                                     customer_name = " - - - "
                                 else
                                     customer_name = customer.getString("name")

                                 if (customer.getString("contact_no").equals("null"))
                                     customer_contact = " - - - "
                                 else
                                     customer_contact = customer.getString("contact_no")
                             }catch (_:Exception){
                                 customer_id = " - - - "
                                 customer_name = " - - - "
                                 customer_contact = " - - - "
                             }
                             Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                             prefManager.setActiveRide(1)
                             if (!status.equals("pending")) {

                                 val alertDialog2 = android.app.AlertDialog.Builder(this)
                                 alertDialog2.setTitle("Thank-you for waiting. Customer has accepted your ride request.")
                                 alertDialog2.setMessage(" Please proceed.")
                                 alertDialog2.setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
                                     addNotification()
                                     /*Toast.makeText(this,"Customer has accepted your ride request.",Toast.LENGTH_SHORT).show()*/

                                     startActivity(
                                         Intent(this, CustomerCityRideDetailActivity::class.java)
                                             .putExtra("booking_id", booking_id.toString())
                                             .putExtra("type", type)
                                             .putExtra("to_location_lat", to_location_lat)
                                             .putExtra("to_location_long", to_location_long)
                                             .putExtra("address_name", address_name)
                                             .putExtra("from_location_lat", from_location_lat)
                                             .putExtra("from_location_long", from_location_long)
                                             .putExtra("from_name", from_name)
                                             .putExtra("date_only", date_only)
                                             .putExtra("time_only", time_only)
                                             .putExtra("ride_id", ride_id)
                                             .putExtra("customer_name", customer_name)
                                             .putExtra("customer_contact", customer_contact)
                                             .putExtra("price", price)
                                             .putExtra("ride_type", type)
                                     )
                                         timer?.cancel()
                                         timer?.onFinish()

                                 }
                                 alertDialog2.setNegativeButton(
                                     "Cancel"
                                 ) { dialog: DialogInterface, which: Int ->
                                     reject()
                                     dialog.cancel()
                                 }
                                 if (!(this@CityRideActivity).isFinishing) {
                                     //show dialog
                                     alertDialog2.show()
                                 }


                             }
                         }
                         //prefManager.setActiveRide(1)
                         // startActivity(Intent(this,DriverDashBoard::class.java))


                     },
                     Response.ErrorListener { error ->
                         Log.d(
                             "CityRideActivity",
                             "error===" + error
                         )
                     }) {
                     @SuppressLint("SuspiciousIndentation")
                     @Throws(AuthFailureError::class)
                     override fun getHeaders(): Map<String, String> {
                         val headers: MutableMap<String, String> = HashMap()


                         headers.put("Content-Type", "application/json; charset=UTF-8");
                         headers.put("Authorization", "Bearer " + prefManager.getToken());
                         return headers
                     }
                 }
             queue.add(jsonOblect)
         }
         else{

         var url = Helper.accept_city_ride_request
         Log.d("CityRideActivity", "Accept status URL===" + url)
         var queue = Volley.newRequestQueue(this)
         var json = JSONObject()
         json.put("ride_id", ride_id)
         Log.d("Ride id", "Ride id===" + json)
         var accepted: Boolean = false

         val jsonOblect: JsonObjectRequest =
             object : JsonObjectRequest(Method.POST, url, json,
                 com.android.volley.Response.Listener<JSONObject?> { response ->
                     // Log.d("CityRideActivityRes", "Accept status response===" + response)

                     var message = response.getString("message")
                     //     Toast.makeText(this@CityRideActivity, ""+message, Toast.LENGTH_LONG).show()

                     if (response != null) {

                         var data = response.getJSONObject("data")

                         var ride = data.getJSONObject("ride")
                         var id = ride.getString("id")
                         var status = ride.getString("status")
                         booking_id = ride.getString("booking_id")
                         type = ride.getString("booking_type")

                         Log.d("RES CITY", "Accept status response===" + response)
                         //   Log.d("CityRideActivity", "id===" + booking_id)

                         var to_location =
                             response.getJSONObject("data").getJSONObject("ride")
                                 .getJSONObject("to_location")
                         to_location_lat = to_location.getString("lat")
                         to_location_long = to_location.getString("lng")
                         address_name = to_location.getString("name")
                         /*     Log.d(
                                         "SendData",
                                         "to_location" + to_location_lat + "\n" + to_location_long + "\n" + address_name
                                     )*/

                         var from_location = response.getJSONObject("data").getJSONObject("ride")
                             .getJSONObject("from_location")
                         from_location_lat = from_location.getString("lat")
                         from_location_long = from_location.getString("lng")
                         from_name = from_location.getString("name")
                         /*       Log.d(
                                         "SendData",
                                         "to_location" + from_location_lat + "\n" + from_location_long + "\n" + from_name
                                     )*/

                         date_only = ride.getString("date_only")
                         time_only = ride.getString("time_only")
                         var customer =
                             response.getJSONObject("data").getJSONObject("customer")
                         customer_id = customer.getString("id")
                         customer_name = customer.getString("name")
                         customer_contact = customer.getString("contact_no")
                         Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                         prefManager.setActiveRide(1)
                         if (!status.equals("pending")) {
                             val alertDialog2 = android.app.AlertDialog.Builder(this)
                             alertDialog2.setTitle("Thank-you for waiting. Customer has accepted your ride request.")
                             alertDialog2.setMessage(" Please proceed.")
                             alertDialog2.setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
                                 addNotification()
                                 /*Toast.makeText(this,"Customer has accepted your ride request.",Toast.LENGTH_SHORT).show()*/
                                 startActivity(
                                     Intent(this, CustomerCityRideDetailActivity::class.java)
                                         .putExtra("booking_id", booking_id.toString())
                                         .putExtra("type", type)
                                         .putExtra("to_location_lat", to_location_lat)
                                         .putExtra("to_location_long", to_location_long)
                                         .putExtra("address_name", address_name)
                                          .putExtra("from_location_lat", from_location_lat)
                                         .putExtra("from_location_long", from_location_long)
                                         .putExtra("from_name", from_name)
                                         .putExtra("date_only", date_only)
                                         .putExtra("time_only", time_only)
                                         .putExtra("ride_id", ride_id)
                                         .putExtra("customer_name", customer_name)
                                         .putExtra("customer_contact", customer_contact)
                                         .putExtra("price", price)
                                         .putExtra("ride_type", type)
                                 )


                             }
                             alertDialog2.setNegativeButton(
                                 "Cancel"
                             ) { dialog: DialogInterface, which: Int ->
                                 reject()
                                 dialog.cancel()
                             }
                             alertDialog2.show()

                         }
                     }
                     //prefManager.setActiveRide(1)
                     // startActivity(Intent(this,DriverDashBoard::class.java))


                 },
                 Response.ErrorListener { error ->
                     Log.d(
                         "CityRideActivity",
                         "error===" + error
                     )
                 }) {
                 @SuppressLint("SuspiciousIndentation")
                 @Throws(AuthFailureError::class)
                 override fun getHeaders(): Map<String, String> {
                     val headers: MutableMap<String, String> = HashMap()


                     headers.put("Content-Type", "application/json; charset=UTF-8");
                     headers.put("Authorization", "Bearer " + prefManager.getToken());
                     return headers
                 }
             }
         queue.add(jsonOblect)
         /*      delay(10000L)
                 }}*/
     }

     }

     private fun addNotification() {
         val mNotificationManager: NotificationManager
         val mBuilder = NotificationCompat.Builder(this, "notify_001")
        // pref.setNotify("true")
         var pendingIntent: PendingIntent? = null
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
             pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
         }else {
             pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
         }
         val bigText = NotificationCompat.BigTextStyle()
         bigText.setBigContentTitle("Booking is Accepted")
         bigText.bigText("Your Booking is Accepted by customer...")
         bigText.setSummaryText("Text in detail")

         mBuilder.setContentIntent(pendingIntent)
         mBuilder.setSmallIcon(R.drawable.appicon)
         mBuilder.setContentTitle("Booking is Accepted")
         mBuilder.setContentText("Your Booking is Accepted by Customer...")
         mBuilder.priority = Notification.PRIORITY_MAX
         mBuilder.setStyle(bigText)

         mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channelId = "notify_001"
             val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH)
             mNotificationManager.createNotificationChannel(channel)
             mBuilder.setChannelId(channelId)
         }

         mNotificationManager.notify(10, mBuilder.build())
     }

     fun reject(){
         //var url="https://test.pearl-developer.com/figo/api/driver-ride/reject-city-ride-request"
         var url=Helper.reject_city_ride_request
         Log.d("CityRideActivity", "Reject status URL===" + url)
         var queue=Volley.newRequestQueue(this)
         var json=JSONObject()
         json.put("ride_request_id",ride_request_id)
         json.put("ride_id",ride_id)


         val jsonOblect: JsonObjectRequest =
             object : JsonObjectRequest(Method.POST, url,json,
                 com.android.volley.Response.Listener<JSONObject?> { response ->
                     Log.d("CityRideActivity", "Reject status response===" + response)
                     var statuss=response.getString("status")
                     if (statuss.equals(true)) {
                         var message=response.getString("message")
                         Toast.makeText(this@CityRideActivity, "rejected"+message, Toast.LENGTH_LONG).show()
                         //finish()
                     }
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

         queue.add(jsonOblect)
         // Toast.makeText(this,"Accepted",Toast.LENGTH_SHORT).show()

         // Toast.makeText(this," This Booking is rejected",Toast.LENGTH_SHORT).show()
         // finish()
         startActivity(
             Intent(
                 this,
                 DriverDashBoard::class.java
             ))
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
            //Log.d("MAPDATA"," data ===="+ data )

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

    override fun onMarkerClick(p0: Marker): Boolean {
        TODO("Not yet implemented")
    }

/*    override fun onLocationChanged(p0: Location) {
*//*        val curLat: Double = p0.getLatitude() //current latitude
        val curLong: Double = p0.getLongitude() //current longitude

        mMap.clear()

        pickupLocation = LatLng(curLat, curLong)
        dropLocation = LatLng(fromLat.toDouble(), fromLong.toDouble())
        val height = 100
        val width = 100
        val bitmapdraw = resources.getDrawable(R.drawable.go_location) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        mMap.addMarker(
            MarkerOptions().position(pickupLocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title("Current Location")
        )
        val bitmapdraw2 = resources.getDrawable(R.drawable.ic_location) as BitmapDrawable
        val b2 = bitmapdraw2.bitmap
        val smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false)
        mMap.addMarker(
            MarkerOptions().position(dropLocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2))
                .title("dropoff")

        )

        val source = "" + curLat + "," + curLong
        val destination = "" + fromLat + "," + fromLong
        Log.e("Origin ", "$source\n Destination $destination")
        GetDirection().execute(source, destination)*//*

    }*/

    fun liveRouting(newlat:Double,newlon:Double){

        mMap.clear()
        dropLocation   = LatLng(testlat, testlon)
        pickupLocation     = LatLng(newlat, newlon)

        val height = 100
        val width = 100
        /*val bitmapdraw = resources.getDrawable(R.drawable.go_location) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)*/
        mMap.addMarker(
            MarkerOptions().position(pickupLocation!!)

                .title("Current Location")
        )
        /*val bitmapdraw2 = resources.getDrawable(R.drawable.ic_location) as BitmapDrawable
        val b2 = bitmapdraw2.bitmap
        val smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false)*/
        mMap.addMarker(
            MarkerOptions().position(dropLocation!!)

                .title("dropoff")

        )

        val source = "" + newlat + "," + newlon
        val destination = "" + testlat + "," + testlon
        Log.e("Origin ", "$source\n Destination $destination")
        //GetDirection().execute(source, destination)
        var url:String=getDirectionURL(pickupLocation!!, dropLocation!!,"AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
        GetDirection(url).execute()
        Handler().postDelayed({
            //do something
        }, 5000)
    }


     override fun onBackPressed() {
         // code here to show dialog
         // reject()
         if(accepted==1) {
             timer?.onFinish()
             timer?.cancel()
         }

         super.onBackPressed() // optional depending on your needs
     }

     override fun onPause() {
         super.onPause()
         /*timer?.cancel()*/ // timer is a reference to my inner CountDownTimer class
         //timer = null
     }
}