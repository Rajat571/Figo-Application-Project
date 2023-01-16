package com.figgo.cabs.FiggoPartner.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.DropAdapter



class PartnerAllRideAdapter(val allrideData:List<AllRideData>): RecyclerView.Adapter<PartnerAllRideAdapter.ViewHolder>() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.allriderecyclerlayout, parent, false)

        val recyclerView2 = view.findViewById<RecyclerView>(R.id.drop_recycler)

        val droparray: List<String> = listOf("z", "zx", "zy", "zr", "zt")
        val view_btn = view.findViewById<TextView>(R.id.allride_viewbtn)

        recyclerView2.adapter = DropAdapter(droparray)
        recyclerView2.layoutManager = LinearLayoutManager(parent.context)
        val cardView = view.findViewById<CardView>(R.id.card)
        var hidden = view.findViewById<LinearLayout>(R.id.hidden_view)
        val ontap = view.findViewById<LinearLayout>(R.id.ontap)

        hidden.visibility = View.GONE
        view_btn.setOnClickListener {
            if (hidden.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                ontap.visibility = View.VISIBLE
                hidden.visibility = View.GONE
                view_btn.text = "View"
            } else {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                ontap.visibility = View.GONE
                hidden.visibility = View.VISIBLE
                view_btn.text = "Hide"
            }
        }




        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val x = allrideData[position]
        holder.time.text = x.time
        holder.local.text = x.locationlocal
        holder.google.text = x.locationgoogle
    }

    override fun getItemCount(): Int {
        return allrideData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.allride_time)
        val local: TextView = itemView.findViewById(R.id.local_location)
        val google: TextView = itemView.findViewById(R.id.google_location)
        // val dropLocation:TextView = itemView.findViewById(R.id.location_tv)
    }
}