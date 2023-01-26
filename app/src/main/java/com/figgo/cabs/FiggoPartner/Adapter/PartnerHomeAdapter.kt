

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.figgodriver.model.HomeData
import com.figgo.cabs.R


class PartnerHomeAdapter(var booking:List<HomeData>):RecyclerView.Adapter<DataHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        return DataHolder(LayoutInflater.from(parent.context).inflate(R.layout.rideset,parent,false))
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
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

//        holder.cancel.setOnClickListener {
//            holder.sedan.text="ASPAKSLA"
//            holder.rideset_view.visibility = View.GONE
//
//        }
    }

    override fun getItemCount(): Int {
        return booking.size
    }
}
class DataHolder(itemView: View):RecyclerView.ViewHolder(itemView){

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

    var rideset_view = itemView.findViewById<LinearLayout>(R.id.rideset_view)
    var cancel = itemView.findViewById<LinearLayout>(R.id.cancel)
}