package com.figgo.cabs.FiggoPartner.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.figgo.cabs.FiggoPartner.Model.PartnerActiveRide
import com.figgo.cabs.FiggoPartner.UI.Partner_Dashboard
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.ViewRideDialog


class PartnerActiveRideAdapter(var context:Context, var datalist:List<PartnerActiveRide>):
    Adapter<PartnerActiveRideAdapter.ActiveRideHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveRideHolder {
        return ActiveRideHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.active_ride_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActiveRideHolder, position: Int) {
       var data=datalist[position]
        holder.ways.text=data.ways
        holder.date.text=data.date
        holder.vieww.text=data.view
        holder.vieww.setOnClickListener {

            val activity = context as Partner_Dashboard

            var dialog= ViewRideDialog()

            dialog.show(activity.supportFragmentManager,"Dialog box")
        }
    }
    override fun getItemCount(): Int {
     return  datalist.size
    }
    class ActiveRideHolder(itemview:View):ViewHolder(itemview){
        var ways=itemview.findViewById<TextView>(R.id.oneway)
        var date=itemview.findViewById<TextView>(R.id.date)
        var vieww=itemview.findViewById<TextView>(R.id.view)
    }
}