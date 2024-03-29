package com.figgo.cabs.figgodriver.Fragment.outstation


import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.ActivityCustomerCityRideDetailBinding
import com.figgo.cabs.databinding.ActivityOutstationCustomerCityRideDetailBinding
import com.figgo.cabs.figgodriver.MapData
import com.figgo.cabs.figgodriver.Service.MyService
import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.figgodriver.UI.StartRideActivity
import com.figgo.cabs.pearllib.Helper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.HashMap

class OutstationCustomerCityRideDetailActivity: AppCompatActivity(), OnMapReadyCallback {
    private var originLatitude: Double =0.0
    private var originLongitude: Double = 0.0
    private var destinationLatitude: Double =0.0
    private var destinationLongitude: Double =0.0
    lateinit var bookingID:String
    lateinit var bookingType:String
    lateinit var pickuplocationTV:String
    lateinit var pickuplocation:String
    lateinit var fareprice:String
    lateinit var dropLocationTV:String
    lateinit var actual_distance:String
    lateinit var dropLocation:String
    private lateinit var mMap: GoogleMap
    var rideId:Int = 0
    lateinit var prefManager: PrefManager
    lateinit var binding:ActivityOutstationCustomerCityRideDetailBinding
    //lateinit var layout_accept_wait:LinearLayout
    //lateinit var layout_customer_city_ride:LinearLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
try {
    binding =
        ActivityOutstationCustomerCityRideDetailBinding.inflate(layoutInflater)
    setContentView(binding.root)
}catch (_:Exception){

}
        binding.outstationBookingCustomer.text=intent.getStringExtra("booking_id")
        binding.outstationBookingType.text=intent.getStringExtra("type")
        binding.outstationPickupLocation.text=intent.getStringExtra("address_name")
        binding.outstationDropLocation.text=intent.getStringExtra("from_name")
        var price=intent.getStringExtra("price")
        binding.outstationPrice.text=price
        actual_distance = intent.getStringExtra("actual_distance").toString()
        Log.d("Actual Distance2","$actual_distance")
        binding.outstationDistance.text=actual_distance
        binding.outstationRideDate.text=intent.getStringExtra("date_only")
        binding.outstationRideTime.text=intent.getStringExtra("time_only")
        originLatitude= intent.getStringExtra("to_location_lat")!!.toDouble()
        originLongitude=intent.getStringExtra("to_location_long")!!.toDouble()
        destinationLatitude=intent.getStringExtra("from_location_lat")!!.toDouble()
        destinationLongitude=intent.getStringExtra("from_location_long")!!.toDouble()

        bookingID = intent.getStringExtra("booking_id")!!
        bookingType = intent.getStringExtra("type")!!
        pickuplocationTV = intent.getStringExtra("address_name")!!
        pickuplocation = intent.getStringExtra("address_name")!!
        dropLocationTV = intent.getStringExtra("from_name")!!
        dropLocation = intent.getStringExtra("from_name")!!
        fareprice = intent.getStringExtra("price")!!
        rideId = intent.getIntExtra("ride_id",0)!!
        var ride_type=intent.getStringExtra("ride_type")



        prefManager=PrefManager(this)
        stopService(Intent(this, MyService::class.java))
        binding.outstationContactTV.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("tel:123")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        prefManager.setdestinationlocation(destinationLatitude.toFloat(),destinationLongitude.toFloat())
        Log.d("CustomerCityRideDetailActivity","$destinationLatitude $destinationLongitude")
        // layout_accept_wait=findViewById(R.id.accept_wait_layout)
        //layout_customer_city_ride=findViewById(R.id.city_ride_activitylayout)
        var mapFragmentt=supportFragmentManager.findFragmentById(R.id.outstation_map) as SupportMapFragment
        mapFragmentt.getMapAsync(this)

        mapFragmentt.getMapAsync {

            mMap = it
            val originLocation = LatLng(prefManager.getlatitude().toDouble(), prefManager.getlongitude().toDouble())
            val height = 80
            val width = 80
            val bitmapdraw = resources.getDrawable(R.drawable.location_green) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
            // val originLocation = LatLng( originLatitude, originLongitude)
            mMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .position(originLocation).title("Current location"))
            val destinationLocation = LatLng(destinationLatitude!!, destinationLongitude!!)
            //val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            mMap.addMarker(MarkerOptions().position(destinationLocation).title("Pickup"))
            val urll = getDirectionURL(originLocation, destinationLocation, "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))

        }

        var start = findViewById<TextView>(R.id.outstation_customer_startbtn)
        var loc= ArrayList<String>()
        loc.add("Zirakpur (Google location)")
        loc.add("Airport Light")
        loc.add("Hallomajra Light")
        loc.add("Sanvg, Ambala")
        loc.add("Haryana, India")
        /*var min=5
        var sec:Int=60
        var finished:Boolean=false
        var timertv = findViewById<TextView>(R.id.timer5min)
        val timer = object: CountDownTimer(5000, 1000) {
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

            }

            override fun onFinish() {
finished=true
                navigatenext()

            }
        }
        timer.start()*/
        // if(finished)

        start.setOnClickListener {
            cancelNotification(this,10)
/*
            startActivity(Intent(this, StartRideActivity::class.java)
                .putExtra("bookingID",bookingID)
                .putExtra("bookingType",bookingType)
                .putExtra("pickup",pickuplocationTV)
                .putExtra("dropLocation",dropLocationTV)
                .putExtra("price",fareprice))
*/


            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.otp_start_layout)
            var submit = dialog.findViewById<Button>(R.id.dialog_submit)
            var cancel = dialog.findViewById<ImageView>(R.id.cancel_icon)
            var name=dialog.findViewById<TextView>(R.id.name)
            var number=dialog.findViewById<TextView>(R.id.mobilenum)
            var pickup=dialog.findViewById<TextView>(R.id.pickup)
            var drop=dialog.findViewById<TextView>(R.id.drop)
            var ridestartotp=dialog.findViewById<EditText>(R.id.ridestartotp)
            name.text=intent.getStringExtra("customer_name")
            if (intent.getStringExtra("customer_name")=="null"){
                name.text="Figgo Customer"
            }
            number.text=intent.getStringExtra("customer_contact")
            pickup.text=intent.getStringExtra("address_name")
            drop.text=intent.getStringExtra("from_name")


            submit.setOnClickListener {
                // val otp1 = "https://test.pearl-developer.com/figo/api/driver-ride/check-ride-otp"

                var URL=Helper.check_ride_otp
                Log.d("VerifyNumber","URL"+URL)
                val queue2 = Volley.newRequestQueue(this@OutstationCustomerCityRideDetailActivity)
                val json2 = JSONObject()
                var otp=ridestartotp.text.toString()
                json2.put("otp", otp)
                Log.d("OTP", "json2===" + json2)

                var jsonObjectRequest=object :JsonObjectRequest(Method.POST,URL,json2,Response.Listener<JSONObject>
                {
                        response ->

                    Log.d("VerifyNumber","OTPresponse"+response)

                    if (response!=null){
                        if (response.getString("status").equals("true")) {
                            //startActivity(Intent(this, StartRideActivity::class.java))
                            startActivity(Intent(this, OutstationStartRideActivity::class.java)
                                .putExtra("bookingID",bookingID)
                                .putExtra("bookingType",bookingType)
                                .putExtra("pickup",pickuplocation)
                                .putExtra("dropLocation",dropLocation)
                                .putExtra("price",fareprice)
                                .putExtra("destinationLatitude",destinationLatitude)
                                .putExtra("destinationLongitude",destinationLongitude))
                        }
                        else{
                            try {
                                Toast.makeText(
                                    this,
                                    "" + response.getString("message"),
                                    Toast.LENGTH_LONG
                                ).show()
                            }catch (_:Exception){

                            }

                        }
                    }
                },object :Response.ErrorListener{
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("VerifyNumber","ERROR"+error)
                        try {
                            Toast.makeText(this@OutstationCustomerCityRideDetailActivity, "Please enter correct OTP", Toast.LENGTH_LONG).show()
                        }catch (_:Exception){

                        }
//                        ridestartotp.setError("wrong otp")
//                        Toast.makeText(this@OutstationCustomerCityRideDetailActivity,"Entered wrong otp",Toast.LENGTH_SHORT).show()
/*                        startActivity(Intent(this@OutstationCustomerCityRideDetailActivity,OutstationStartRideActivity::class.java)
                            .putExtra("bookingID",bookingID)
                            .putExtra("bookingType",bookingType)
                            .putExtra("pickup",pickuplocation)
                            .putExtra("dropLocation",dropLocation)
                            .putExtra("price",fareprice)
                            .putExtra("destinationLatitude",destinationLatitude)
                            .putExtra("destinationLongitude",destinationLongitude))*/

                    }
                }){

                    @SuppressLint("SuspiciousIndentation")
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers: MutableMap<String, String> = HashMap()
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        headers.put("Authorization", "Bearer " + prefManager.getToken());
                        headers.put("Accept","application/vnd.api+json");

                        return headers
                    }
                }
                queue2.add(jsonObjectRequest)

                //Toast.makeText(this,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
    fun cancelNotification(ctx: Context, notifyId: Int) {
        val ns: String = Context.NOTIFICATION_SERVICE
        val nMgr = ctx.getSystemService(ns) as NotificationManager
        nMgr.cancel(notifyId)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0

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
/*fun navigatenext(){

        //startActivity(Intent(this, DriverDashBoard::class.java))
    layout_accept_wait.visibility=View.GONE
    layout_customer_city_ride.visibility=View.VISIBLE
    }*/


    override fun onBackPressed() {
        val alertDialog2 = AlertDialog.Builder(
            this
        )
        alertDialog2.setTitle("Alert..")
        alertDialog2.setMessage("Are you sure you want to cancel this ride?")
        alertDialog2.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            finishAffinity()
            reject()
            //startActivity(Intent(this,DriverDashBoard::class.java))
        }
        alertDialog2.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        alertDialog2.show()
        //       finish();
    }

    fun reject(){
        //var url="https://test.pearl-developer.com/figo/api/driver-ride/reject-city-ride-request"
        var url=Helper.reject_city_ride_request
        Log.d("CityRideActivity", "Reject status URL===" + url)
        var queue=Volley.newRequestQueue(this)
        var json=JSONObject()
        if(rideId==0)
            json.put("ride_request_id",prefManager.getRideID())
        else
            json.put("ride_request_id",rideId)
        val jsonOblect: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url,json,
                com.android.volley.Response.Listener<JSONObject?> { response ->
                    Log.d("CityRideActivity", "Reject status response===" + response)
                    var statuss=response.getString("status")
                    if (statuss.equals(true)) {
                        var message=response.getString("message")
                        Toast.makeText(this@OutstationCustomerCityRideDetailActivity, "rejected"+message, Toast.LENGTH_LONG).show()
                        //finish()


                    }
                    // Get your json response and convert it to whatever you want.
                }, object : com.android.volley.Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("CityRideActivity", "error===" + error)
                        //  Toast.makeText(this@CustomerCityRideDetailActivity, "Something went wrong!", Toast.LENGTH_LONG).show()
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


}