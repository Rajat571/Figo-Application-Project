package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.figgo.cabs.R

import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.figgodriver.Fragment.ViewRideDialog
import com.figgo.cabs.figgodriver.Fragment.prefManager
import com.figgo.cabs.figgodriver.UI.StartRideActivity
import com.figgo.cabs.figgodriver.model.ActiveRideData

class ActiveRideAdapter(var context:Context,var datalist:List<ActiveRideData>):
    Adapter<ActiveRideAdapter.ActiveRideHolder>() {


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
            context.startActivity(Intent(context,StartRideActivity::class.java)
                .putExtra("bookingID",data.booking_id)
                .putExtra("bookingType",data.type)
                .putExtra("pickup",data.from_name)
                .putExtra("dropLocation",data.to_name)
                .putExtra("price",data.price)
                .putExtra("destinationLatitude",data.to_location_lat)
                .putExtra("destinationLongitude",data.to_location_long))
            prefManager.setRideID(data.ride_id.toInt())
            prefManager.setdestinationlocation(data.to_location_lat.toFloat(),data.to_location_long.toFloat())
        }

    }
    override fun getItemCount(): Int {
     return  datalist.size
    }
    class ActiveRideHolder(itemview:View):ViewHolder(itemview){
        var card = itemview.findViewById<CardView>(R.id.activeride_card)
        var booking_id = itemview.findViewById<TextView>(R.id.active_booking_customer)
        var type = itemview.findViewById<TextView>(R.id.active_booking_type)
        var pickup_location = itemview.findViewById<TextView>(R.id.active_pickup_location)
        var drop_location = itemview.findViewById<TextView>(R.id.active_drop_location)

    }
}