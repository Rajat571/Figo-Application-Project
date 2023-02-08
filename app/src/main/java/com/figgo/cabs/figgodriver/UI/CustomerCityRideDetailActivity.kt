package com.figgo.cabs.figgodriver.UI

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.ActivityCustomerCityRideDetailBinding
import com.figgo.cabs.figgodriver.MapData
import com.figgo.cabs.pearllib.Helper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class CustomerCityRideDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private var originLatitude: Double =0.0
    private var originLongitude: Double = 0.0
    private var destinationLatitude: Double =0.0
    private var destinationLongitude: Double =0.0
    private lateinit var mMap: GoogleMap
    lateinit var prefManager: PrefManager
    lateinit var binding:ActivityCustomerCityRideDetailBinding
    lateinit var layout_accept_wait:LinearLayout
    lateinit var layout_customer_city_ride:LinearLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
       binding=DataBindingUtil.setContentView(this,R.layout.activity_customer_city_ride_detail)
        binding.bookingCustomer.text=intent.getStringExtra("booking_id")
        binding.bookingType.text=intent.getStringExtra("type")
        binding.pickupLocation.text=intent.getStringExtra("address_name")
        binding.dropLocation.text=intent.getStringExtra("from_name")
        var price=intent.getStringExtra("300")
        binding.rideDate.text=intent.getStringExtra("date_only")
        binding.allrideTime.text=intent.getStringExtra("time_only")
        originLatitude= intent.getStringExtra("to_location_lat")!!.toDouble()
        originLongitude=intent.getStringExtra("to_location_long")!!.toDouble()
        destinationLatitude=intent.getStringExtra("from_location_lat")!!.toDouble()
        destinationLongitude=intent.getStringExtra("from_location_long")!!.toDouble()
        prefManager=PrefManager(this)

        layout_accept_wait=findViewById(R.id.accept_wait_layout)
        layout_customer_city_ride=findViewById(R.id.city_ride_activitylayout)
        var mapFragmentt=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragmentt.getMapAsync(this)

        mapFragmentt.getMapAsync {

            mMap = it
            val originLocation = LatLng(prefManager.getlatitude().toDouble(), prefManager.getlongitude().toDouble())
            // val originLocation = LatLng( originLatitude, originLongitude)
            mMap.addMarker(MarkerOptions().position(originLocation).title("Current location"))
            val destinationLocation = LatLng(destinationLatitude!!, destinationLongitude!!)
            //val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            mMap.addMarker(MarkerOptions().position(destinationLocation).title("Pickup"))
            val urll = getDirectionURL(originLocation, destinationLocation, "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))

        }

        var start = findViewById<TextView>(R.id.customer_startbtn)
        var loc= ArrayList<String>()
        loc.add("Zirakpur (Google location)")
        loc.add("Airport Light")
        loc.add("Hallomajra Light")
        loc.add("Sanvg, Ambala")
        loc.add("Haryana, India")
        var min=5
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
        timer.start()
       // if(finished)

        start.setOnClickListener {
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
                var otp1=Helper.check_ride_otp
                Log.d("VerifyNumber","URL"+otp1)
                val queue2 = Volley.newRequestQueue(this@CustomerCityRideDetailActivity)
                val json2 = JSONObject()
                var otp=ridestartotp.text.toString()
                json2.put("otp", otp.toInt())
                Log.d("OTP", "json2===" + json2)

      var jsonObjectRequest=object :JsonObjectRequest(Method.POST,otp1,json2,Response.Listener<JSONObject>
      {response ->

          Log.d("VerifyNumber","OTPresponse"+response)
          if (response!=null){
              if (response.getString("status").equals("true")) {
                  startActivity(Intent(this, StartRideActivity::class.java))
              }
              else{
                  Toast.makeText(this,""+response.getString("message"),Toast.LENGTH_LONG).show()
              }
          }
      },object :Response.ErrorListener{
          override fun onErrorResponse(error: VolleyError?) {
              Log.d("VerifyNumber","ERROR"+error)
              ridestartotp.setError("wrong otp")
              Toast.makeText(this@CustomerCityRideDetailActivity,"Entered wrong otp",Toast.LENGTH_SHORT).show()
          }
      }){

          @SuppressLint("SuspiciousIndentation")
          @Throws(AuthFailureError::class)
          override fun getHeaders(): Map<String, String> {
              val headers: MutableMap<String, String> = HashMap()
              headers.put("Content-Type", "application/json; charset=UTF-8");
              headers.put("Authorization", "Bearer " + prefManager.getToken())
              return headers
          }
      }
      queue2.add(jsonObjectRequest)

               /* Toast.makeText(this,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()*/
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
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
fun navigatenext(){

        //startActivity(Intent(this, DriverDashBoard::class.java))
    layout_accept_wait.visibility=View.GONE
    layout_customer_city_ride.visibility=View.VISIBLE
    }
}