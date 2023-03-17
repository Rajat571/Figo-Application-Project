package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.UI.CityRideActivity
import com.figgo.cabs.figgodriver.model.CityCurrentRidesList
import java.io.File

class OutstationRideAdapter(var context: Context, var ridelist:List<CityCurrentRidesList>):
    RecyclerView.Adapter<OutstationRideAdapter.OutstationRideHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutstationRideHolder {
        return OutstationRideHolder(LayoutInflater.from(context).inflate(R.layout.outstationridelistlayout,parent,false))
    }

    override fun onBindViewHolder(holder: OutstationRideHolder, position: Int) {

        val data=ridelist[position]
        /*
         holder.customer_name.text=data.cutomer_name*/

        holder.location_from.text=data.from
        holder.location_to.text=data.to
        holder.customer_time.text=data.time
        holder.customer_date.text=data.date

        // holder.fare_price.text=data.fare_price

        holder.itemView.setOnClickListener {
            holder.rideCardview.background = context.getDrawable(R.drawable.booking_box_outline)
            //holder.rideCardview.setCardBackgroundColor(Color.GREEN)
/*            context.startActivity(
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
                .putExtra("customer_booking_id",data.cutomer_name)
                .putExtra("ride_id",data.ride_id)
                    .putExtra("Outstaion_id",data.y)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))*/
            //deleteCache(context)

        }
    }

    override fun getItemCount()=ridelist.size

    class OutstationRideHolder(itemView: View):androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        /*

          var customer_name=itemView.findViewById<TextView>(R.id.customer_name)*/
        var location_to=itemView.findViewById<TextView>(R.id.outlocation_to)
        var location_from=itemView.findViewById<TextView>(R.id.outlocation_from)
        var customer_time=itemView.findViewById<TextView>(R.id.outcustomer_time)
        // var fare_price=itemView.findViewById<TextView>(R.id.fare_price)
        var rideCardview=itemView.findViewById<CardView>(R.id.outride_cardview)
        var customer_date=itemView.findViewById<TextView>(R.id.outcustomer_date)
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