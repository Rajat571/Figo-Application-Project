package com.example.FiggoPartner.UI

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FiggoPartner.Adapter.PartnerActiveRideAdapter
import com.example.FiggoPartner.Model.PartnerActiveRide
import com.example.figgodriver.R
import com.example.figgodriver.databinding.ActivityPartnerDashboardBinding


class Partner_Dashboard : AppCompatActivity() {
    lateinit var binding: ActivityPartnerDashboardBinding
    lateinit var activeRideAdapter: PartnerActiveRideAdapter
    var datalist=ArrayList<PartnerActiveRide>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))

       binding=DataBindingUtil.setContentView(this, R.layout.activity_partner_dashboard)
        binding.activeDriverList.layoutManager=LinearLayoutManager(this)
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))

        activeRideAdapter= PartnerActiveRideAdapter(this,datalist)
        binding.activeDriverList.adapter=activeRideAdapter

    }
}