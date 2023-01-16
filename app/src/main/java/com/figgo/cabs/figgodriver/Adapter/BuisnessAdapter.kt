package com.figgo.cabs.figgodriver.Adapter

import android.R.id
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.model.BuisnessAd


class BuisnessAdapter(var data:List<BuisnessAd>, var v:View): RecyclerView.Adapter<BuisnessAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        var inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.figgo_buisness,parent,false)
        return Viewholder(view)

    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.im.setImageResource(data[position].imageId)
        holder.im.setOnClickListener {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data[position].url))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(data[position].url)
            )
            try {
                v.context.startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                v.context.startActivity(webIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    class Viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
        var im = itemView.findViewById<ImageView>(R.id.buisness_imV)
    }

}
