package com.figgo.cabs.figgodriver.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R

class CustomerCityAdapter(private val loc:List<String>): RecyclerView.Adapter<CustomerCityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.dropcustomerlayout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvloc?.text = loc[position]
        if (position==0||position==loc.size-1){
            holder.im?.setImageResource(R.drawable.ic_baseline_location_on_24)
        }
    }

    override fun getItemCount(): Int {
        return loc.size
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvloc  = itemView?.findViewById<TextView>(R.id.dc_location_tv)
        val im = itemView?.findViewById<ImageView>(R.id.dc_im)
    }
}
