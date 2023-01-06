package com.pearl.figgodriver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
//import com.example.figgodriver.Fragment.AccountDetailsFragment
import com.pearl.figgodriver.R
import kotlinx.android.synthetic.main.active_ride_layout.*
import kotlinx.android.synthetic.main.fragment_driver_cab_details.view.*
import kotlinx.android.synthetic.main.top_layout.view.*

//C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools..\
//.\adb connect 192.168.137.43

class AccountDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        var ad = com.pearl.figgodriver.Fragment.AccountDetails()
        supportFragmentManager.beginTransaction().add(R.id.account_detailsFrame,ad).commit()
    }
}