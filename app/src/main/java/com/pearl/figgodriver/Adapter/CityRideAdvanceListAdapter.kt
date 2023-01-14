package com.pearl.figgodriver.Adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.pearl.figgodriver.AdvanceCityRideActivity
import com.pearl.figgodriver.CityRideActivity
import com.pearl.figgodriver.R
import com.pearl.figgodriver.model.CityAdvanceRideList
import com.pearl.figgodriver.model.CityCurrentRidesList

class CityRideAdvanceListAdapter(var context:Context, var advanceridelist:List<CityAdvanceRideList>) :
    Adapter<CityRideAdvanceListAdapter.AdvanceRideHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvanceRideHolder {
        return AdvanceRideHolder(LayoutInflater.from(context).inflate(R.layout.city_advance_ride_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: AdvanceRideHolder, position: Int) {
        var data=advanceridelist[position]
        holder.customer_date.text=data.date
        holder.customer_time.text=data.time
        holder.customer_name.text=data.booking_id
        holder.location_from.text=data.from
        holder.location_to.text=data.to
        holder.fare_price.text=data.fare_price
        Log.d(" CityRideAdvanceListAdapter","date===="+data.date)

        holder.itemView.setOnClickListener {
            holder.rideCardview.setCardBackgroundColor(Color.GREEN)

            context.startActivity(
                Intent(context, CityRideActivity::class.java)
                .putExtra("location_to",data.to)
                .putExtra("customer_date",data.date)
                .putExtra("customer_time",data.time)
                .putExtra("current_lat",data.current_lat)
                .putExtra("current_long",data.current_long)
                .putExtra("des_lat",data.des_lat)
                .putExtra("des_long",data.des_long)
                .putExtra("customer_booking_id",data.booking_id)
                .putExtra("ride_id",data.ride_id)
                .putExtra("ride_request_id","3"))

        }
    }

    override fun getItemCount()=advanceridelist.size

    class AdvanceRideHolder(itemview:View):androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview)
    {
        var customer_date=itemView.findViewById<TextView>(R.id.advance_date)
        var customer_time=itemView.findViewById<TextView>(R.id.advance_time)
        var customer_name=itemView.findViewById<TextView>(R.id.advance_booking_id)
        var location_to=itemView.findViewById<TextView>(R.id.advance_location_to)
        var location_from=itemView.findViewById<TextView>(R.id.advance_location_from)
        var fare_price=itemView.findViewById<TextView>(R.id.advance_fare_price)
        var rideCardview=itemView.findViewById<CardView>(R.id.advance_ride_cardview)
    }

}