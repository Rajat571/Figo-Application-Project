package com.pearl.figgodriver.Fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pearl.figgodriver.Adapter.CustomerCityAdapter
import com.pearl.figgodriver.R
import com.pearl.figgodriver.StartRideActivity
import com.pearl.figgodriver.databinding.FragmentCustomerCityRideDetailsBinding

class CustomerCityRideDetails : Fragment() {
    lateinit var binding:FragmentCustomerCityRideDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
    binding=DataBindingUtil.inflate(inflater,R.layout.fragment_customer_city_ride_details, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // var recycler = view.findViewById<RecyclerView>(R.id.customer_drop_recycler)
        var start = view.findViewById<TextView>(R.id.customer_startbtn)
        var loc= ArrayList<String>()
        loc.add("Zirakpur (Google location)")
        loc.add("Airport Light")
        loc.add("Hallomajra Light")
        loc.add("Sanvg, Ambala")
        loc.add("Haryana, India")

//        recycler.adapter = CustomerCityAdapter(loc)
//        recycler.layoutManager=LinearLayoutManager(requireContext())
        start.setOnClickListener {
            val dialog = Dialog(view.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.otp_start_layout)
            var submit = dialog.findViewById<Button>(R.id.dialog_submit)
            var cancel = dialog.findViewById<ImageView>(R.id.cancel_icon)
            submit.setOnClickListener {
                context?.startActivity(Intent(requireContext(),StartRideActivity::class.java))

                Toast.makeText(view.context,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }

}