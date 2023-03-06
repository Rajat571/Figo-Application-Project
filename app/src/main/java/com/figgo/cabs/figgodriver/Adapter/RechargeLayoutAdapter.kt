package com.figgo.cabs.figgodriver.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.UI.PaymentRechargeActivity
import com.figgo.cabs.figgodriver.model.Recharge
import org.json.JSONException
import org.json.JSONObject

class RechargeLayoutAdapter(var context:Context ,var data:List<Recharge>): RecyclerView.Adapter<ViewHolder>() {
lateinit var activity:Activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflator = LayoutInflater.from(parent.context)
        var view = inflator.inflate(R.layout.rechargelayout,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.book_limit.text = data[position].booking_limit
        holder.ride_request.text = data[position].ride_request.toString()
        holder.recharge_amount.text = data[position].recharge_amount.toString()
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,PaymentRechargeActivity::class.java)
                .putExtra("amount",data[position].recharge_amount)
                .putExtra("id",data[position].recharge_id))
        }
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