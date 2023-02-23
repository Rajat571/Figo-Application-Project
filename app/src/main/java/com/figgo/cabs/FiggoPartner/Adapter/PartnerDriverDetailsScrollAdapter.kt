package com.figgo.cabs.FiggoPartner.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Model.PartnerDriverView
import com.figgo.cabs.R

class PartnerDriverDetailsScrollAdapter(var data:List<PartnerDriverView>):
    RecyclerView.Adapter<PartnerDriverDetailsScrollHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerDriverDetailsScrollHolder {
        return PartnerDriverDetailsScrollHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_drivers_scroll_layout,parent,false))
    }

    override fun onBindViewHolder(holder: PartnerDriverDetailsScrollHolder, position: Int) {
        var x=data[position]
        holder.name.text = x.name
        holder.dl.text = x.dl
        holder.mobile.text = x.mobile
    }

    override fun getItemCount(): Int {
        return data.size
    }
}


class PartnerDriverDetailsScrollHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var name = itemView.findViewById<TextView>(R.id.driver_scroll_name)
    var dl = itemView.findViewById<TextView>(R.id.driver_scroll_dl)
    var mobile = itemView.findViewById<TextView>(R.id.driver_scroll_number)
}