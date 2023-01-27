package com.figgo.cabs.figgodriver.Adapter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.CityRideActivity
import com.figgo.cabs.figgodriver.model.CityCurrentRidesList

class CityRideCurrentListAdapter(var context:Context, var ridelist:List<CityCurrentRidesList>):
    Adapter<CityRideCurrentListAdapter.CityRideHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityRideHolder {
        return CityRideHolder(LayoutInflater.from(context).inflate(R.layout.city_current_ride_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: CityRideHolder, position: Int) {

     var data=ridelist[position]
        holder.customer_date.text=data.date
        holder.customer_time.text=data.time
        holder.customer_name.text=data.cutomer_name
        holder.location_from.text=data.from
        holder.location_to.text=data.to
        holder.fare_price.text=data.fare_price

        holder.itemView.setOnClickListener {

            holder.rideCardview.setBackgroundResource(R.drawable.booking_box_outline)
                context.startActivity(Intent(context, CityRideActivity::class.java)
                    .putExtra("location_to",data.to)
                    .putExtra("customer_date",data.date)
                    .putExtra("customer_time",data.time)
                    .putExtra("current_lat",data.current_lat)
                    .putExtra("current_long",data.current_long)
                    .putExtra("des_lat",data.des_lat)
                    .putExtra("des_long",data.des_long)
                    .putExtra("customer_booking_id",data.cutomer_name)
                    .putExtra("ride_id",data.ride_id)
                    .putExtra("ride_request_id",data.ride_request_id)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun getItemCount()=ridelist.size

    class CityRideHolder(itemView: View):androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        var customer_date=itemView.findViewById<TextView>(R.id.customer_date)
        var customer_time=itemView.findViewById<TextView>(R.id.customer_time)
        var customer_name=itemView.findViewById<TextView>(R.id.customer_name)
        var location_to=itemView.findViewById<TextView>(R.id.location_to)
        var location_from=itemView.findViewById<TextView>(R.id.location_from)
        var fare_price=itemView.findViewById<TextView>(R.id.fare_price)
        var rideCardview=itemView.findViewById<CardView>(R.id.ride_cardview)
    }
}