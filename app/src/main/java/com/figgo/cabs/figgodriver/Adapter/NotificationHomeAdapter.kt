package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.figgo.cabs.R

class NotificationHomeAdapter(var context: Context,var list: List<Int>):Adapter<NotificationHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(LayoutInflater.from(parent.context).inflate(R.layout.individualnotificationlayout,parent,false))
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
        TODO("Not yet implemented")
    }

}
class NotificationHolder(itemView: View):ViewHolder(itemView){

}