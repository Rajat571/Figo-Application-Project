package com.figgo.cabs.FiggoPartner.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Model.PartnerDriverView
import com.figgo.cabs.R

class PartnerDriversDetailAdapter(var data:List<PartnerDriverView>):
    RecyclerView.Adapter<PartnerDriverHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerDriverHolder {
        return PartnerDriverHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_drivers_layout,parent,false))
    }

    override fun onBindViewHolder(holder: PartnerDriverHolder, position: Int) {
        var x=data[position]
        holder.name.text = x.name
        holder.dl.text = x.dl
        holder.mobile.text = x.mobile
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class PartnerDriverHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var name = itemView.findViewById<TextView>(R.id.partnerview_drivername)
    var dl = itemView.findViewById<TextView>(R.id.partner_driverDL)
    var mobile = itemView.findViewById<TextView>(R.id.partnerview_drivernumber)
}