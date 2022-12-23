package com.pearl.figgodriver.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.pearl.figgodriver.R
import com.pearl.figgodriver.model.SpinnerObj
import java.util.ArrayList

class SpinnerAdapter(context: Context?, resource: Int, myarrayList: ArrayList<SpinnerObj>) :
    ArrayAdapter<SpinnerObj?>(context!!, resource) {
    private val myarrayList: ArrayList<SpinnerObj>

    init {
        this.myarrayList = myarrayList
    }

    override fun getDropDownView(position: Int, convertView: View, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getItem(position: Int): SpinnerObj? {
        return myarrayList[position]
    }

   // var count: Int = myarrayList.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val model: SpinnerObj? = getItem(position)
        val spinnerRow: View =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner, parent, false)
        val name: TextView = spinnerRow.findViewById<TextView>(R.id.txt_name)
        name.setText(model?.name)
        return spinnerRow
    }
}