package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.R
import kotlinx.android.synthetic.main.fragment_all_ride_r_s.*

class OutstationAdapter(val allrideData:List<AllRideData>): RecyclerView.Adapter<OutstationAdapter.ViewHolder>() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(R.layout.outstationrecycler, parent, false)

        val recyclerView2 = view.findViewById<RecyclerView>(R.id.drop_recycler)

        val droparray: List<String> = listOf("z", "zx", "zy", "zr", "zt")
        val view_btn = view.findViewById<TextView>(R.id.allride_viewbtn)
        val start_btn = view.findViewById<TextView>(R.id.outstation_startbtn)

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
            }}
            start_btn.setOnClickListener {

                val dialog = Dialog(view.context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.otp_start_layout)
                var submit = dialog.findViewById<Button>(R.id.dialog_submit)
                var cancel = dialog.findViewById<ImageView>(R.id.cancel_icon)
                submit.setOnClickListener {
                    Toast.makeText(view.context,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
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

