package com.figgo.cabs.figgodriver.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.R

import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.figgodriver.Fragment.ViewRideDialog
import com.figgo.cabs.figgodriver.Fragment.prefManager
import com.figgo.cabs.figgodriver.UI.CustomerCityRideDetailActivity
import com.figgo.cabs.figgodriver.UI.StartRideActivity
import com.figgo.cabs.figgodriver.model.ActiveRideData
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject
import java.util.HashMap

class ActiveRideAdapter(var context:Context,var datalist:List<ActiveRideData>):
    Adapter<ActiveRideAdapter.ActiveRideHolder>() {
    var ride_id:String="" 
    var ride_type:String="" 
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
    var price=""



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveRideHolder {
        return ActiveRideHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_activeride,parent,false))
    }

    override fun onBindViewHolder(holder: ActiveRideHolder, position: Int) {
       var data=datalist[position]
        holder.booking_id.text = data.booking_id
        holder.type.text = data.type
        holder.pickup_location.text=data.from_name
        holder.drop_location.text = data.to_name
        holder.card.setOnClickListener {
            ride_id = data.ride_id
            ride_type = data.type

            acceptwait()
            /*
            context.startActivity(Intent(context,StartRideActivity::class.java)
                .putExtra("bookingID",data.booking_id)
                .putExtra("bookingType",data.type)
                .putExtra("location_to",data.to_name)
                .putExtra("from_name",data.from_name)
                .putExtra("date_only",data.date_only)
                .putExtra("time_only",data.time_only)
                .putExtra("from_location_lat",data.from_location_lat)
                .putExtra("from_location_long",data.from_location_long)
                .putExtra("to_location_lat",data.to_location_lat)
                .putExtra("to_location_long",data.to_location_long)
                .putExtra("customer_booking_id",data.booking_id)
                .putExtra("ride_id",data.ride_id)
                .putExtra("ride_request_id","3")
                .putExtra("pickup",data.from_name)
                .putExtra("dropLocation",data.to_name)
                .putExtra("price",data.price)
                .putExtra("destinationLatitude",data.to_location_lat)
                .putExtra("destinationLongitude",data.to_location_long)
                .putExtra("type",data.type))*/
            prefManager.setRideID(data.ride_id.toInt())
            prefManager.setdestinationlocation(data.to_location_lat.toFloat(),data.to_location_long.toFloat())
        }

    }
    override fun getItemCount(): Int {
     return  datalist.size
    }
    private fun acceptwait() {
        Log.d("RES CITY", "Enter Functions")
        //   Toast.makeText(context,"Accept API RUNIING",Toast.LENGTH_SHORT).show()
        var min = 5
        var sec: Int = 60

        var finished: Boolean = false
        //var timertv = findViewById<TextView>(R.id.timer5min)

        if (ride_type == "advance") {
            var url= Helper.accept_advance_ride_request

            Log.d("CityRideActivity", "Advance Accept status URL===" + url)
            var queue = Volley.newRequestQueue(context)
            var json = JSONObject()
            json.put("ride_id", ride_id)
            Log.d("Ride id", "Ride id===" + json)


            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(
                    Method.POST, url, json,
                    com.android.volley.Response.Listener<JSONObject?> { response ->

                        if (response != null) {

                            var data = response.getJSONObject("data")

                            var ride = data.getJSONObject("ride")
                            var id = ride.getString("id")
                            var status = ride.getString("status")
                            booking_id = ride.getString("booking_id")
                            type = ride.getString("type")

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
                            var customer =
                                response.getJSONObject("data").getJSONObject("customer")
                            customer_id = customer.getString("id")
                            customer_name = customer.getString("name")
                            customer_contact = customer.getString("contact_no")
                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                            prefManager.setActiveRide(1)
                                    /*Toast.makeText(context,"Customer has accepted your ride request.",Toast.LENGTH_SHORT).show()*/
                                    if (outstation_id==2){}
                                    else{
                                        context.startActivity(
                                            Intent(context, CustomerCityRideDetailActivity::class.java)
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
                        }
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
            var queue = Volley.newRequestQueue(context)
            var json = JSONObject()
            json.put("ride_id", ride_id)
            Log.d("Ride id", "Ride id===" + json)
            var accepted: Boolean = false

            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(
                    Method.POST, url, json,
                    com.android.volley.Response.Listener<JSONObject?> { response ->
                        // Log.d("CityRideActivityRes", "Accept status response===" + response)

                        var message = response.getString("message")
                        //     Toast.makeText(context@CityRideActivity, ""+message, Toast.LENGTH_LONG).show()

                        if (response != null) {

                            var data = response.getJSONObject("data")

                            var ride = data.getJSONObject("ride")
                            var id = ride.getString("id")
                            var status = ride.getString("status")
                            booking_id = ride.getString("booking_id")
                            type = ride.getString("type")

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
                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                            prefManager.setActiveRide(1)
                                    /*Toast.makeText(context,"Customer has accepted your ride request.",Toast.LENGTH_SHORT).show()*/
                                    context.startActivity(
                                        Intent(context, CustomerCityRideDetailActivity::class.java)
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
                        //prefManager.setActiveRide(1)
                        // startActivity(Intent(context,DriverDashBoard::class.java))


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
    class ActiveRideHolder(itemview:View):ViewHolder(itemview){
        var card = itemview.findViewById<CardView>(R.id.activeride_card)
        var booking_id = itemview.findViewById<TextView>(R.id.active_booking_customer)
        var type = itemview.findViewById<TextView>(R.id.active_booking_type)
        var pickup_location = itemview.findViewById<TextView>(R.id.active_pickup_location)
        var drop_location = itemview.findViewById<TextView>(R.id.active_drop_location)

    }
}