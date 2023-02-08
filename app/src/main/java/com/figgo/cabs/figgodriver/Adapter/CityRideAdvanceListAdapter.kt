package com.figgo.cabs.figgodriver.Adapter


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
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.UI.CityRideActivity
import com.figgo.cabs.figgodriver.model.CityAdvanceRideList
import java.io.File




class CityRideAdvanceListAdapter(var context:Context, var advanceridelist:List<CityAdvanceRideList>) :
    Adapter<CityRideAdvanceListAdapter.AdvanceRideHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvanceRideHolder {
        return AdvanceRideHolder(LayoutInflater.from(context).inflate(R.layout.city_current_ride_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: AdvanceRideHolder, position: Int) {
        var data=advanceridelist[position]
      /*

        holder.customer_name.text=data.booking_id*/
        holder.customer_time.text=data.time
        holder.location_from.text=data.from
        holder.location_to.text=data.to
        holder.customer_date.text=data.date
       // holder.fare_price.text=data.fare_price
        Log.d(" CityRideAdvanceListAdapter","date===="+data.date)
        holder.rideCardview.setCardBackgroundColor(Color.parseColor("#ffffff"))

        holder.itemView.setOnClickListener {
            holder.rideCardview.setBackground(context.getDrawable(R.drawable.booking_box_outline))
            //holder.rideCardview.setBackgroundResource()

            context.startActivity(
                Intent(context, CityRideActivity::class.java)
                .putExtra("location_to",data.to)
                    .putExtra("location_from",data.from)
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
       // deleteCache(context)
    }

    override fun getItemCount()=advanceridelist.size

    class AdvanceRideHolder(itemview:View):androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview)
    {
      /*

        var customer_name=itemView.findViewById<TextView>(R.id.advance_booking_id)*/
      var location_to=itemView.findViewById<TextView>(R.id.location_to)
        var location_from=itemView.findViewById<TextView>(R.id.location_from)
      //  var fare_price=itemView.findViewById<TextView>(R.id.advance_fare_price)
        var rideCardview=itemView.findViewById<CardView>(R.id.ride_cardview)
        var customer_time=itemView.findViewById<TextView>(R.id.customer_time)
        var customer_date=itemView.findViewById<TextView>(R.id.customer_date)
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