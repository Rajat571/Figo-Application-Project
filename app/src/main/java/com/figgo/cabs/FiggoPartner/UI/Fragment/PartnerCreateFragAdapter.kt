package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Model.Location
import com.figgo.cabs.R


class PartnerCreateFragAdapter (val citynames:List<Location>): RecyclerView.Adapter<PartnerCreateFragAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view  = layoutInflater.inflate(R.layout.createfragrecyclerview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data=citynames[position]
        holder.tv1.text =data.city_name
        holder.lccation.setImageResource(data.location)

    }

    override fun getItemCount(): Int {
        return citynames.size
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tv1: TextView = itemView.findViewById(R.id.textView1)
        var lccation=itemView.findViewById<ImageView>(R.id.location)
        //var tv2:TextView = itemView.findViewById(R.id.textView2)

    }

}
