package com.figgo.cabs.figgodriver

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.figgo.cabs.R
import com.figgo.cabs.databinding.ActivityCustomerCityRideDetailBinding

class CustomerCityRideDetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityCustomerCityRideDetailBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this,R.layout.activity_customer_city_ride_detail)
        var booking=intent.getStringExtra("booking_id")
        var booking_type=intent.getStringExtra("type")
        var pick_up_location=intent.getStringExtra("address_name")
        var drop_location=intent.getStringExtra("from_name")
        var price=intent.getStringExtra("300")
        var date=intent.getStringExtra("date_only")
        var time=intent.getStringExtra("time_only")
        binding.bookingCustomer.text=booking
        binding.bookingType.text=booking_type
        binding.pickupLocation.text=pick_up_location
        binding.dropLocation.text=drop_location
        binding.price.text=price
        binding.allrideTime.text=time


        var start = findViewById<TextView>(R.id.customer_startbtn)
        var loc= ArrayList<String>()
        loc.add("Zirakpur (Google location)")
        loc.add("Airport Light")
        loc.add("Hallomajra Light")
        loc.add("Sanvg, Ambala")
        loc.add("Haryana, India")


//        recycler.adapter = CustomerCityAdapter(loc)
//        recycler.layoutManager=LinearLayoutManager(requireContext())
        start.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.otp_start_layout)
            var submit = dialog.findViewById<Button>(R.id.dialog_submit)
            var cancel = dialog.findViewById<ImageView>(R.id.cancel_icon)
            submit.setOnClickListener {
                startActivity(Intent(this, StartRideActivity::class.java))

                Toast.makeText(this,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}