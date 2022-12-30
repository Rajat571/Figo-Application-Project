package com.example.figgodriver.Fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pearl.figgodriver.R
import com.pearl.figgodriver.model.Sedan


class SedanAdapter(var booking:List<Sedan>):RecyclerView.Adapter<ViewHolder>() {
    lateinit var x:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rideset,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val x = booking[position]
        holder.sedan.text = x.sedan
        holder.sub_sean.text = x.sub_sedan
        holder.distance.text=x.dist
        holder.date.text = x.date
        holder.time.text = x.time
        holder.mainadd.text = x.add1
        holder.addsub1.text = x.subadd1
        holder.addsub2.text = x.subadd2
        holder.addsub3.text = x.subadd3
        holder.bottomadd.text = x.lastadd
        holder.perkm.text = x.extra_km.toString()
        holder.submit.setOnClickListener {
            Toast.makeText(holder.x.context,"Submitted",Toast.LENGTH_SHORT).show()
        }
        holder.accept.setOnClickListener {
            Toast.makeText(holder.x.context,"Accepted",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return booking.size
    }
}
class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    var sedan = itemView.findViewById<TextView>(R.id.sedan)
    var sub_sean = itemView.findViewById<TextView>(R.id.etios)
    var distance = itemView.findViewById<TextView>(R.id.numbertext)
    var date = itemView.findViewById<TextView>(R.id.date)
    var time = itemView.findViewById<TextView>(R.id.time)
    var mainadd = itemView.findViewById<TextView>(R.id.sectorchndigargh)
    var addsub1 = itemView.findViewById<TextView>(R.id.ambala)
    var addsub2 = itemView.findViewById<TextView>(R.id.panipath)
    var addsub3 = itemView.findViewById<TextView>(R.id.delhi)
    var bottomadd = itemView.findViewById<TextView>(R.id.chandigarh)
    var perkm = itemView.findViewById<TextView>(R.id.total_km)
    var driver_price = itemView.findViewById<EditText>(R.id.driver_price)
    var submit = itemView.findViewById<TextView>(R.id.rideset_submit)
    var accept = itemView.findViewById<TextView>(R.id.rideset_accept)
    var x=itemView

    }



