package com.example.figgodriver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.figgodriver.Adapter.ActiveRideAdapter
import com.example.figgodriver.databinding.ActivityPartnerDashboardBinding
import com.example.figgodriver.model.ActiveRide

class Partner_Dashboard : AppCompatActivity() {
    lateinit var binding:ActivityPartnerDashboardBinding
    lateinit var activeRideAdapter: ActiveRideAdapter
    var datalist=ArrayList<ActiveRide>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))

       binding=DataBindingUtil.setContentView(this,R.layout.activity_partner_dashboard)
        binding.activeDriverList.layoutManager=LinearLayoutManager(this)
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))

        activeRideAdapter= ActiveRideAdapter(this,datalist)
        binding.activeDriverList.adapter=activeRideAdapter

    }
}