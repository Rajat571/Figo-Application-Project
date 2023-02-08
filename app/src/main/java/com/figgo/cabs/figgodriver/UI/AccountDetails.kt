package com.figgo.cabs.figgodriver.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.AccountDetails
//import com.example.figgodriver.Fragment.AccountDetailsFragment
import kotlinx.android.synthetic.main.active_ride_layout.*
import kotlinx.android.synthetic.main.fragment_driver_cab_details.view.*
import kotlinx.android.synthetic.main.top_layout.view.*

//C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools..\
//.\adb connect 192.168.137.43

class AccountDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        var ad = AccountDetails()
        supportFragmentManager.beginTransaction().add(R.id.account_detailsFrame,ad).commit()
    }
}