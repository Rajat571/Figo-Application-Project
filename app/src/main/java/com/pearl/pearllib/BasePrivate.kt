package com.pearl.pearllib

import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pearl.figgodriver.ChooseUserActivity

abstract class BasePrivate : BaseClass() {
    var minteger = 0
    val REQUEST_LOCATION = 1
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude: String? = null
    var bestProvider: String? = null
    var criteria: Criteria? = null
    var coin_balance: TextView? = null
    var shiprocketToken: String? = null
    fun CheckSession(baseApbcContext: Context?, activityIn: AppCompatActivity) {
        session = Session(baseApbcContext!!)
        val token = session!!.token
        var session_available = false
        if (session!!.hasSession!!) {
            if (!token!!.isEmpty()) {
                session_available = true
                printLogs(
                    "BasePublic ",
                    "CheckSession ",
                    "is_session_available - $session_available"
                )
            } else {
                session_available = false
                printLogs(
                    "BasePublic ",
                    "CheckSession ",
                    "is_session_available - $session_available"
                )
            }
        } else {
            session_available = false
            printLogs("BasePublic ", "CheckSession ", "is_session_available - $session_available")
        }
        if (!session_available) {
            val intent1 = Intent(baseApbcContext, ChooseUserActivity::class.java)
            startActivity(intent1)
            activityIn.finish()
        }
    }


    /*fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }*/


}