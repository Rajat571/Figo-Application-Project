package com.figgo.cabs.figgodriver.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.AccountDetails
//import com.example.figgodriver.Fragment.AccountDetailsFragment

//C:\Users\Admin\AppData\Local\Android\Sdk\platform-tools..\
//.\adb connect 192.168.137.43

class DriverRechargeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        var ad = AccountDetails()
        supportFragmentManager.beginTransaction().add(R.id.account_detailsFrame,ad).commit()
    }
}