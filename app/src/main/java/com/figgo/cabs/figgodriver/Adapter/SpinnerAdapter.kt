package com.figgo.cabs.figgodriver.Adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.model.SpinnerObj

class SpinnerAdapter(var context: Context, var myarrayList: List<SpinnerObj>) : BaseAdapter(){


    /* override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
         if (convertView==null){}
         val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner, parent, false)
         val model: SpinnerObj? = getItem(position)
         // spinner_image.setImageResource(flag)
         val name: TextView = view.findViewById<TextView>(R.id.txt_name)
         name.setText(model?.name)
         return view
     }

     override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
         return getCustomView(position, parent)
     }

     private fun getCustomView(position: Int, parent: ViewGroup): View {
         val model: SpinnerObj? = getItem(position)
         val spinnerRow: View =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner, parent, false)
         val name: TextView = spinnerRow.findViewById<TextView>(R.id.txt_name)
         name.setText(model?.name)
         return spinnerRow
     }*/

    /*

    override fun getItem(position: Int): SpinnerObj? {
        return myarrayList[position]
    }


//  var count: Int = myarrayList.size

    override fun getCount(): Int {
        return myarrayList.size
    }

*/


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view= LayoutInflater.from(context).inflate(R.layout.row_spinner,p2,false)
        var data= ItemHolder(view)
        data.spinner_txt.text=myarrayList[p0].name


        return view
    }

    override fun getItem(p0: Int): SpinnerObj {
        return myarrayList[p0]
    }

    override fun getCount(): Int {
        return myarrayList.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    private class ItemHolder(row: View) {
        val spinner_txt=row.findViewById<TextView>(R.id.txt_name)

    }}