package com.figgo.cabs.figgodriver.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.outstation.OutstationCityRideActivity
import com.figgo.cabs.figgodriver.model.CityCurrentRidesList
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject
import java.io.File
import java.util.ArrayList
import java.util.HashMap

class OutstationRideAdapter(var context: Context, var ridelist:List<CityCurrentRidesList>):
    RecyclerView.Adapter<OutstationRideAdapter.OutstationRideHolder>() {

    lateinit var prefManager: PrefManager
    var ridelists = ArrayList<CityCurrentRidesList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutstationRideHolder {
        return OutstationRideHolder(LayoutInflater.from(context).inflate(R.layout.outstationridelistlayout,parent,false))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: OutstationRideHolder, position: Int) {
        prefManager = PrefManager(context)
        val data=ridelist[position]
        /*
         holder.customer_name.text=data.cutomer_name*/

        if (data.status.equals("pending")){

            holder.payment_status.setTextColor(Color.parseColor("#ff0000"))
        }else{
            holder.payment_status.setTextColor(Color.parseColor("#00ff00"))
        }
        holder.location_from.text=data.from
        holder.location_to.text=data.to
        holder.customer_time.text=data.time
        holder.customer_date.text=data.date
        holder.bookingId.text=data.cutomer_name
         holder.fare_price.text=data.fare_price

        holder.payment_status.text =data.status

      /*  holder.itemView.setOnClickListener {
            holder.rideCardview.background = context.getDrawable(R.drawable.booking_box_outline)
            //holder.rideCardview.setCardBackgroundColor(Color.GREEN)
          *//*  context.startActivity(
                Intent(context, CityRideActivity::class.java)
                .putExtra("location_to",data.to)
                .putExtra("location_from",data.from)
                .putExtra("price",data.fare_price)
                .putExtra("customer_date",data.date)
                .putExtra("customer_time",data.time)
                .putExtra("current_lat",data.current_lat)
                .putExtra("current_long",data.current_long)
                .putExtra("des_lat",data.des_lat)
                .putExtra("des_long",data.des_long)
                .putExtra("customer_booking_id",data.booking_id)
                .putExtra("ride_id",data.ride_id)
                    .putExtra("Outstaion_id",data.y)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))*//*
            //deleteCache(context)

        }*/

            holder.detailBtn.setOnClickListener {


                holder.innerLL.setVisibility(View.VISIBLE)
                holder.closeIV.setVisibility(View.VISIBLE)
                holder.detailBtn.setVisibility(View.GONE)
            }

        holder.closeIV.setOnClickListener {

           holder.innerLL.setVisibility(View.GONE)
           holder.closeIV.setVisibility(View.GONE)
           holder.detailBtn.setVisibility(View.VISIBLE)
        }

        holder.acceptBtn.setOnClickListener {

            holder.processBar.setVisibility(View.VISIBLE)
            holder.acceptBtn.setVisibility(View.GONE)

            try {
                ridelists.clear()
                // val URL = "https://test.pearl-developer.com/figo/api/driver-ride/get-city-ride-request"
                var URL = Helper.outstation_oneway_accept_request
                Log.d("OutStationCityRideFragment", "outstation_oneway_accept_request Current===$URL")
                val queue = Volley.newRequestQueue(context)
                val json = JSONObject()
                json.put("ride_id",data.ride_id)

                val jsonOblect: JsonObjectRequest =
                    object : JsonObjectRequest(
                        Method.POST, URL, json,
                        Response.Listener<JSONObject?> { response ->
                            Log.d("CITY_RIDE_FRAGMENT Current", "response===$response")
                            if (response != null) {
                                context.startActivity(
                                    Intent(context, OutstationCityRideActivity::class.java)
                                        .putExtra("location_to",data.to)
                                        .putExtra("location_from",data.from)
                                        .putExtra("price",data.fare_price)
                                        .putExtra("customer_date",data.date)
                                        .putExtra("customer_time",data.time)
                                        .putExtra("current_lat",data.current_lat)
                                        .putExtra("current_long",data.current_long)
                                        .putExtra("des_lat",data.des_lat)
                                        .putExtra("des_long",data.des_long)
                                        .putExtra("customer_booking_id",data.cutomer_name)
                                        .putExtra("ride_id",data.ride_id)
                                        .putExtra("Outstaion_id",data.y)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            }
                            // Get your json response and convert it to whatever you want.
                        },
                        Response.ErrorListener { error -> Log.d("SendData", "error===$error") }) {
                        @SuppressLint("SuspiciousIndentation")
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val headers: MutableMap<String, String> = HashMap()
                            headers.put("Content-Type", "application/json; charset=UTF-8")
                            headers.put("Authorization", "Bearer " + prefManager.getToken())
                            return headers
                        }
                    }

                queue.add(jsonOblect)
            } catch (e: Exception) {

            }


        }

    }

    override fun getItemCount()=ridelist.size

    class OutstationRideHolder(itemView: View):androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        /*

          var customer_name=itemView.findViewById<TextView>(R.id.customer_name)*/
        var location_to=itemView.findViewById<TextView>(R.id.outlocation_to)
        var location_from=itemView.findViewById<TextView>(R.id.outlocation_from)
        var customer_time=itemView.findViewById<TextView>(R.id.outcustomer_time)
         var fare_price=itemView.findViewById<TextView>(R.id.fare_price)
        var bookingId = itemView.findViewById<TextView>(R.id.bookingId)
        var rideCardview=itemView.findViewById<CardView>(R.id.outride_cardview)
        var customer_date=itemView.findViewById<TextView>(R.id.outcustomer_date)

        var detailBtn =itemView.findViewById<TextView>(R.id.btnDetailId)
        var innerLL =itemView.findViewById<LinearLayout>(R.id.innerLL)
        var closeIV =itemView.findViewById<TextView>(R.id.cancel)
        var payment_status = itemView.findViewById<TextView>(R.id.outcustomer_status)

        var acceptBtn =itemView.findViewById<TextView>(R.id.accept_city_ride_btn)
        var processBar =itemView.findViewById<ProgressBar>(R.id.outstation_progressbar)



//        var user_id = itemView.findViewById<TextView>(R.id.userId)
    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}