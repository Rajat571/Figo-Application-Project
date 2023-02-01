package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R

class RideHistoryAdapter(var data: List<String>,var x:Int,var context:Context):RecyclerView.Adapter<RideHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideHistoryHolder {
        return RideHistoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.ridehistory_table_adapter,parent,false))

    }

    override fun onBindViewHolder(holder: RideHistoryHolder, position: Int) {
        holder.boxTV.text= data[position]
        if (x==0){
            //holder.boxTV.setTextColor(Color.WHITE)
            holder.block.setBackgroundColor(context.getColor(R.color.exel))
            holder.boxTV.setTypeface(null, Typeface.BOLD);
            holder.boxTV.setTextColor(context.getColor(R.color.white))
        }else if(x%2==0) {
            holder.block.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
class RideHistoryHolder(itemView: View):ViewHolder(itemView){
    var boxTV=itemView.findViewById<TextView>(R.id.tablecontentTV)
    var block = itemView.findViewById<LinearLayout>(R.id.blocklinear)
}