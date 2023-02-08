package com.figgo.cabs.figgodriver.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.RequestDetails
import com.figgo.cabs.figgodriver.Fragment.allRideRS
import com.figgo.cabs.figgodriver.Fragment.createRS

class ShareRide : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_ride)
        var create: Button = findViewById<Button>(R.id.rideshare_create)
        var allride = findViewById<Button>(R.id.rideshare_allride)

        var createFrag = createRS()
        var allRidefrag = allRideRS()
        var driverdetails = RequestDetails()
        var details = findViewById<Button>(R.id.Request_Detals)
        setfragment(createFrag)
        create.setOnClickListener{
            setfragment(createFrag)
            allride.setBackgroundResource(R.color.purple_200)
            allride.setTextColor(resources.getColor(R.color.white))
            create.setBackgroundColor(resources.getColor(R.color.white))
            create.text = "CREATE"
            create.setTextColor(resources.getColor(R.color.orange))
        }
        allride.setOnClickListener {
            setfragment(allRidefrag)
            create.setBackgroundResource(R.color.purple_200)
            create.setTextColor(resources.getColor(R.color.white))
            allride.setBackgroundColor(resources.getColor(R.color.white))
            allride.setTextColor(resources.getColor(R.color.orange))
        }
        details.setOnClickListener {
            setfragment(driverdetails)
            create.setBackgroundColor(resources.getColor(R.color.white))
            create.setTextColor(resources.getColor(R.color.orange))
            create.text = "DETAILS"
            allride.setBackgroundResource(R.color.purple_200)
            allride.setTextColor(resources.getColor(R.color.white))
        }


    }

    private fun setfragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameRS,frag)
            commit()
        }
    }
}