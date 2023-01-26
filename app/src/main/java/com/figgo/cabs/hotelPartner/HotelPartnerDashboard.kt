package com.figgo.cabs.hotelPartner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.navigation.NavController
import com.figgo.cabs.R


class HotelPartnerDashboard : AppCompatActivity() {
    private lateinit var navController: NavController
    var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_partner_dashboard)
        var frame = findViewById<FrameLayout>(R.id.hotelFrame)



    }
}