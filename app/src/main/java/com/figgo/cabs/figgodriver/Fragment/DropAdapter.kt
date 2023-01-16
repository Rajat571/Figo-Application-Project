package com.figgo.cabs.figgodriver.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R


//lateinit var tvloc:TextView
class DropAdapter(private val loc:List<String>):RecyclerView.Adapter<DropAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.droprecycler,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvloc?.text = loc[position]
    }

    override fun getItemCount(): Int {
        return loc.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
      val tvloc  = itemView?.findViewById<TextView>(R.id.textView1)
    }
}
