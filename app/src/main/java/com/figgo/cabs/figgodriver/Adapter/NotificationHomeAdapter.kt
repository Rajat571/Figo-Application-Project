package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.model.NotificationData

class NotificationHomeAdapter(var context: Context,var list: List<NotificationData>):Adapter<NotificationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(LayoutInflater.from(parent.context).inflate(R.layout.individualnotificationlayout,parent,false))
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.header.text = list[position].header
        holder.desc.text = list[position].description
        holder.time.text = list[position].time
        if (position%2==0)
            holder.card.setCardBackgroundColor(context.resources.getColor(R.color.white))
    }

    override fun getItemCount(): Int {
        return list.size
        TODO("Not yet implemented")
    }

}
class NotificationHolder(itemView: View):ViewHolder(itemView){
    var header = itemView.findViewById<TextView>(R.id.notificationheader)
    var desc = itemView.findViewById<TextView>(R.id.notificationdesc)
    var time = itemView.findViewById<TextView>(R.id.notificationtime)
    var card = itemView.findViewById<CardView>(R.id.notificationcard)
}