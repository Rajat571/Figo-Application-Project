package com.figgo.cabs.figgodriver.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.model.Recharge

class RechargeLayoutAdapter(var data:List<Recharge>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflator = LayoutInflater.from(parent.context)
        var view = inflator.inflate(R.layout.rechargelayout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.book_limit.text = data[position].booking_limit
        holder.ride_request.text = data[position].ride_request.toString()
        holder.recharge_amount.text = data[position].recharge_amount.toString()
    }

    override fun getItemCount(): Int {
    return data.size
    }

}
open class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    var book_limit = itemView.findViewById<TextView>(R.id.bookingLimit)
    var ride_request = itemView.findViewById<TextView>(R.id.riderequest)
    var recharge_amount =  itemView.findViewById<TextView>(R.id.rechargeamount)

}