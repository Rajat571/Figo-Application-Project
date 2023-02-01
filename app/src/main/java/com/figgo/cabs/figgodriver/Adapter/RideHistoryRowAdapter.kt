package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R

class RideHistoryRowAdapter(var data: ArrayList<List<String>>,var content:Context): RecyclerView.Adapter<RideHistoryRowHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideHistoryRowHolder {
        return RideHistoryRowHolder(LayoutInflater.from(parent.context).inflate(R.layout.ridehistoryheader,parent,false))

    }

    override fun onBindViewHolder(holder: RideHistoryRowHolder, position: Int) {
        holder.recyclerView.adapter=RideHistoryAdapter(data[position],position,content)
        holder.recyclerView.layoutManager=LinearLayoutManager(content,LinearLayoutManager.HORIZONTAL,false)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
class RideHistoryRowHolder(itemView: View):ViewHolder(itemView){
    var recyclerView = itemView.findViewById<RecyclerView>(R.id.ridehistoryRowRecycler)
}