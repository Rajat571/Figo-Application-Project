package com.pearl.figgodriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
//import com.example.figgodriver.Fragment.AccountDetailsFragment
import com.pearl.figgodriver.R

class AccountDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        var frame = findViewById<FrameLayout>(R.id.account_detailsFrame)
//        var ad = AccountDetailsFragment()
//        supportFragmentManager.beginTransaction().add(R.id.account_detailsFrame,ad).commit()
    }
}