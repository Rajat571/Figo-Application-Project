package com.pearl.figgodriver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
//import com.example.figgodriver.Fragment.AccountDetailsFragment
import com.pearl.figgodriver.R
import kotlinx.android.synthetic.main.active_ride_layout.*
import kotlinx.android.synthetic.main.fragment_driver_cab_details.view.*
import kotlinx.android.synthetic.main.top_layout.view.*

class AccountDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        var frame = findViewById<FrameLayout>(R.id.account_detailsFrame)
        var topLayout = findViewById<LinearLayout>(R.id.layout)
        var off_toggle = topLayout.toggle_off
        var on_toggle = topLayout.toggle_on
//        topLayout.setOnTouchListener { , motionEvent ->  }
        topLayout.toggle_off.setOnClickListener {
            off_toggle.setBackgroundColor(Color.RED)
            on_toggle.setBackgroundColor(Color.WHITE)
            Toast.makeText(this,"ASASAS",Toast.LENGTH_SHORT).show()
        }
        topLayout.toggle_on.setOnClickListener {
            on_toggle.setBackgroundColor(Color.GREEN)
            off_toggle.setBackgroundColor(Color.WHITE)
        }
        var ad = com.pearl.figgodriver.Fragment.AccountDetails()
        supportFragmentManager.beginTransaction().add(R.id.account_detailsFrame,ad).commit()
    }
}