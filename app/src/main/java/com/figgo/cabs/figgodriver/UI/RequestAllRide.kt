package com.figgo.cabs.figgodriver.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.AllRideAdapter
import com.figgo.cabs.figgodriver.Fragment.allRideRS

class RequestAllRide : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_all_ride)

        //var f1 = RequestDetailsOne()
        //setfragment(f1)
        val allrideData = ArrayList<AllRideData>();
        var main1 = findViewById<LinearLayout>(R.id.main_1)
        var topbutton = findViewById<LinearLayout>(R.id.top_buttons)
        var details = findViewById<LinearLayout>(R.id.details_view)
        var allride = findViewById<ConstraintLayout>(R.id.allrideview)
        var details_button = findViewById<Button>(R.id.details_button)
        var allride_button = findViewById<Button>(R.id.allride_button)
//        var details_view = findViewById<LinearLayout>(R.id.details_view)
//        var allride_view = findViewById<LinearLayout>(R.id.details_view)
        var submit = findViewById<Button>(R.id.sumit)
        var allRidefrag = allRideRS()
        main1.visibility = View.VISIBLE
        topbutton.visibility = View.GONE
        details.visibility = View.GONE
        allride.visibility = View.GONE

        submit.setOnClickListener {
            main1.visibility = View.GONE
            topbutton.visibility = View.VISIBLE
            details.visibility = View.VISIBLE
            allride.visibility = View.GONE
        }

        details_button.setOnClickListener{
            details.visibility = View.VISIBLE
                allride.visibility = View.GONE
        }

        allride_button.setOnClickListener {
            details.visibility = View.GONE
            allride.visibility = View.VISIBLE
            //setfragment(allRidefrag)

            allrideData.add(AllRideData("6:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            allrideData.add(AllRideData("7:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            allrideData.add(AllRideData("8:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            val recyclerView1 = findViewById<RecyclerView>(R.id.frameRSall)
            //  val recyclerView2 = view.findViewById<RecyclerView>(R.id.drop_recycler)
            recyclerView1.adapter = AllRideAdapter(allrideData)
            recyclerView1.layoutManager = LinearLayoutManager(this)

        }




    }
    private fun setfragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameRS,frag)
            commit()
        }
    }


}