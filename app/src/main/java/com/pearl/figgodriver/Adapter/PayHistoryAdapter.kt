package com.pearl.figgodriver.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pearl.figgodriver.R
import com.pearl.figgodriver.model.PaymentHistoryModel
import org.w3c.dom.Text

class PayHistoryAdapter(var data:List<PaymentHistoryModel>):RecyclerView.Adapter<PaymentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHolder {
        return PaymentHolder(LayoutInflater.from(parent.context).inflate(R.layout.hisotrylayout,parent,false))
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        var x = data[position]
        holder.date.text=x.paymentdate
        holder.amount.text = "Rs."+x.amount
        if(x.walletbank==0) {
            holder.amount.setTextColor(Color.parseColor("#FF0000"))
            holder.sendpayment.visibility=View.GONE
        }
        else {
            holder.amount.setTextColor(Color.parseColor("#00FF00"))
            holder.sendpayment.visibility=View.VISIBLE

        }
        holder.subhistory1.text = x.payment1
        holder.subhistory2.text = x.payment2
        holder.subhistory3.text = x.payment3
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
class PaymentHolder(itemView: View):ViewHolder(itemView){
    var date = itemView.findViewById<TextView>(R.id.history_date)
    var amount = itemView.findViewById<TextView>(R.id.history_amount)
    var subhistory1 = itemView.findViewById<TextView>(R.id.history_detail1)
    var subhistory2 = itemView.findViewById<TextView>(R.id.history_detail2)
    var sendpayment = itemView.findViewById<ImageView>(R.id.sendpayment)
    var subhistory3 = itemView.findViewById<TextView>(R.id.history_detail3)
}