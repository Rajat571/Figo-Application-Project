package com.figgo.cabs.figgodriver.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.model.HistoryAdd


class HistoryAdapter(var context:Activity, var historyList:List<HistoryAdd>): Adapter<HistoryAdapter.HistoryHolder>() {
    lateinit var pref: PrefManager

    class HistoryHolder(itemview: View):ViewHolder(itemview)
    {

        var address = itemview.findViewById<TextView>(R.id.address)

    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.history_layout,parent,false))
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        pref = PrefManager(context)
     var data=historyList[position]
      //  holder.cab.setImageResource(data.cab)
        holder.address.text=data.address
        holder.address?.setOnClickListener {
          if (pref.getType().equals("1")){
              pref.setToLatL(data.lat)
              pref.setToLngL(data.lng)

          }else  if (pref.getType().equals("2")){
               pref.setToLatM(data.lat)
               pref.setToLngM(data.lng)

          }else if (pref.getTypeC().equals("1")){
              pref.setToLatLC(data.lat)
              pref.setToLngLC(data.lng)


          }else  if (pref.getTypeC().equals("2")){
              pref.setToLatMC(data.lat)
              pref.setToLngMC(data.lng)


          }
            context.finish()


        }




    }

    override fun getItemCount(): Int {
       return historyList.size
    }
}