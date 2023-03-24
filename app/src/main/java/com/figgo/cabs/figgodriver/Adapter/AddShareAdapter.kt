package com.figgo.cabs.figgodriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.model.LIstModel


class AddShareAdapter(val context: Context, val arrCard: ArrayList<LIstModel>):
    RecyclerView.Adapter<AddShareAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listText: TextView
        var close = itemView.findViewById<ImageView>(R.id.deleteIV)
        init {
            listText = itemView.findViewById(R.id.textName)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list,parent,false))
    }

    override fun getItemCount(): Int {
        return arrCard.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data=arrCard[position]
        holder.listText.setText(data.placeName)
        holder.close.setOnClickListener {
try {
    arrCard.removeAt(position)
}catch (_:Exception){

}
            notifyItemRemoved(position)

        }
    }

}