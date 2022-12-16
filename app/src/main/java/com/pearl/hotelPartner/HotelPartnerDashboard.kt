package com.pearl.hotelPartner

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavController
import com.pearl.figgodriver.R

class HotelPartnerDashboard : AppCompatActivity() {
    private lateinit var navController: NavController
    var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_partner_dashboard)
        var frame = findViewById<FrameLayout>(R.id.hotelFrame)



    }
}