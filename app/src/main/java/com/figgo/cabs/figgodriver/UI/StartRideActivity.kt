package com.figgo.cabs.figgodriver.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
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
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.ActivityStartRideBinding
import com.figgo.cabs.figgodriver.Location
import com.figgo.cabs.figgodriver.MapData
import com.figgo.cabs.figgodriver.Service.FireBaseService
import com.figgo.cabs.figgodriver.Service.MyService
import com.figgo.cabs.pearllib.Helper
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.otp_start_layout.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

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
    lateinit var rideComplete:CardView
    lateinit var bookingID:TextView
    lateinit var bookingType:TextView
    lateinit var pickuplocation:TextView
    lateinit var fareprice:TextView
    lateinit var dropLocationTV:TextView
    lateinit var price:String
    var endRoute:Boolean = false
    private lateinit var mMap: GoogleMap
    lateinit var binding: ActivityStartRideBinding
    private var timer:CountDownTimer?=null
    lateinit var prefManager: PrefManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var rideId:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this, R.layout.activity_start_ride)
        rideComplete = findViewById(R.id.ride_complete)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
prefManager= PrefManager(this)

        bookingID  = findViewById(R.id.end_booking_customer)
        bookingType = findViewById(R.id.end_booking_type)
        pickuplocation = findViewById(R.id.end_pickup_location)
        fareprice = findViewById(R.id.end_price)
        dropLocationTV = findViewById(R.id.end_drop_location)

        bookingType.text = intent.getStringExtra("bookingType")
        bookingID.text = intent.getStringExtra("bookingID")
        pickuplocation.text = intent.getStringExtra("pickup")
        fareprice.text = intent.getStringExtra("price")
        price = intent.getStringExtra("price").toString()
        dropLocationTV.text = intent.getStringExtra("dropLocation")

        startService(Intent(this,FireBaseService::class.java))
        rideId = prefManager.getRideID()
        Log.d("RideID ","$rideId")
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
        rideComplete.setOnClickListener {
            rideDetails()
        }

        val ai: ApplicationInfo = applicationContext.packageManager
            ?.getApplicationInfo(applicationContext.applicationContext!!.packageName, PackageManager.GET_META_DATA)!!
        val value = ai.metaData["com.google.android.geo.${R.string.Google_maps_key}"]
        val apiKey = value.toString()
        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext.applicationContext,apiKey)
        }

/*        val database = FirebaseDatabase.getInstance()
        val customerRef = database.getReference("1070 customer")
        val driverRef = database.getReference("1070 customer")*/

var count:Int = 0
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapend) as SupportMapFragment
        updateRoute()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
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

           /* while(!endRoute) {*/


                timer = object: CountDownTimer(9000000, 2000) {
                    override fun onTick(millisUntilFinished: Long) {
                        updateRoute()
                        liveRouting(originLatitude, originLongitude, customerLAT, customerLON)
                    }

                    override fun onFinish() {
                        TODO("Not yet implemented")
                    }
                }
                (timer as CountDownTimer).start()
                Thread.sleep(2000L)

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 11F))
            

        }
    }

    private fun updateRoute(){
        originLatitude = prefManager.getlatitude().toDouble()
        originLongitude = prefManager.getlongitude().toDouble()
        customerLAT = prefManager.getCustLat().toDouble()
        customerLON = prefManager.getCustLon().toDouble()

    }
    private fun rideDetails() {
    var url = Helper.ride_details
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.completeridedetails)
        //var customer_name = dialog.findViewById<TextView>(R.id.rdcomplete_customer_name)
        var rideInfoArray:JSONArray
        var rideInfo:JSONObject
        var rideInfoone:JSONObject
        var date = dialog.findViewById<TextView>(R.id.rdcomplete_date)
        var distance = dialog.findViewById<TextView>(R.id.rdcomplete_distance)
        var to = dialog.findViewById<TextView>(R.id.rdcomplete_destination)
        var from = dialog.findViewById<TextView>(R.id.rdcomplete_from)
        var priceTV = dialog.findViewById<TextView>(R.id.rdcomplete_price)
        var status = dialog.findViewById<TextView>(R.id.rdcomplete_status)
        var tID = dialog.findViewById<TextView>(R.id.rdcomplete_transaction)
        var endMSG = dialog.findViewById<TextView>(R.id.rdcomplete_message)
        var submit = dialog.findViewById<Button>(R.id.rdcomplete_ok)
        var cancel = dialog.findViewById<ImageView>(R.id.rdcomplete_cancel)
        var queue=Volley.newRequestQueue(this)
        priceTV.text = price
        var json = JSONObject()
        json.put("ride_id",rideId)
        var jsonObject: JsonObjectRequest = object : JsonObjectRequest(Method.POST,url,json,{
            if (it!=null){
                Log.d("$this","Res = $it")
                rideInfoArray = it.getJSONArray("ride-info")
                rideInfo = rideInfoArray.getJSONObject(0)
              //  rideInfoone = rideInfoArray.getJSONObject(1)
                date.text = rideInfo.getString("date_only")
                distance.text = rideInfo.getString("actual_distance")
                to.text = rideInfo.getJSONObject("to_location").getString("name")
                from.text = rideInfo.getJSONObject("from_location").getString("name")
                //priceTV.text = rideInfoone.getString("price")
                //status.text = it.getString()
                tID.text = rideInfo.getString("transaction_id")
                if (rideInfo.getString("transaction_id").equals("null")){
                    status.text="Pending"
                    status.setTextColor(resources.getColor(R.color.red))
                    endMSG.text="Please take payment from customer on ride complete."
                }
                else{
                    status.text="Done"
                    status.setTextColor(resources.getColor(R.color.black))
                    endMSG.text=""
                }
                //endMSG.text = it.getString()

            }
        },{

        }){
            @SuppressLint("SuspiciousIndentation")
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", "Bearer " + prefManager.getToken());
                return headers
            }
        }
        queue.add(jsonObject)
        submit.setOnClickListener {
            //Toast.makeText(view.context,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()
            rideFinish()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun rideFinish() {
        var url = Helper.ride_complete
        val dialog = Dialog(this)
        endRoute=true
        stopService(Intent(this,FireBaseService::class.java))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ridecompleteok)
        var okButton = dialog.findViewById<Button>(R.id.final_ridecomplete)
        okButton.setOnClickListener {
            startActivity(Intent(this,DriverDashBoard::class.java))
        }
        var queue=Volley.newRequestQueue(this)
        var json = JSONObject()
        json.put("ride_id",rideId)
        var jsonObject: JsonObjectRequest = object : JsonObjectRequest(Method.POST,url,json,{
            if (it!=null){
               Toast.makeText(this,"Ride Successfully Completed",Toast.LENGTH_SHORT).show()


                dialog.show()
            }
        },{

        }){
            @SuppressLint("SuspiciousIndentation")
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", "Bearer " + prefManager.getToken());
                return headers
            }
        }
        queue.add(jsonObject)

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
            //Log.d("SEND DATA"," data ===="+ data )

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
        customerLocation = LatLng(cust_lat,cust_lon)
        //customerLocation = LatLng(cust_lat)
        val height = 80
        val width = 80
        val bitmapdraw = resources.getDrawable(R.drawable.ic_drivercab) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        mMap.addMarker(
            MarkerOptions().position(driverlocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title("My Location")
        )
        val bitmapdraw2 = resources.getDrawable(R.drawable.ic_destination) as BitmapDrawable
        val b2 = bitmapdraw2.bitmap
        val smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false)
        mMap.addMarker(
            MarkerOptions().position(dropLocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2))
                .title("drop-off")
        )
        val bitdraw3 = resources.getDrawable(R.drawable.ic_customerpin) as BitmapDrawable
        val b3 = bitdraw3.bitmap
        val smallMarker3 = Bitmap.createScaledBitmap(b3,width,height,false)
        mMap.addMarker(
            MarkerOptions().position(customerLocation!!)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker3))
                .title("customer")
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

    /*fun setCurrentLatLon(){
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
    }*/
}