package com.example.figgodriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.figgodriver.Fragment.RequestDetailsOne

class RequestAllRide : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_all_ride)

        var f1 = RequestDetailsOne()
        setfragment(f1)
    }

    private fun setfragment(frag: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.reqeustdetails_frame,frag)
            commit()
        }
    }
}