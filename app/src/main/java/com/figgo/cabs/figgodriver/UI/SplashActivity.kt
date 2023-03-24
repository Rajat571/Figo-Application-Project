package com.figgo.cabs.figgodriver.UI

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Service.MyService
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

class SplashActivity : AppCompatActivity() {

    private val REQUEST_LOCATION = 1
    private lateinit var locationRequest: LocationRequest
    var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=101
    private val REQUEST_CHECK_SETTINGS: Int=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))

        var become_the_luxury=findViewById<TextView>(R.id.become_the_luxury)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_side)
        become_the_luxury.startAnimation(slideAnimation)
        grantLocPer()
    }


    fun grantLocPer() {

        if (isLocationPermissionGranted()) {
            checkLocationService()
        }

        else {
            ActivityCompat.requestPermissions(
                this@SplashActivity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

        }
    }

    fun checkLocationService() {

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        // builder.setAlwaysShow(true);
        val client = LocationServices.getSettingsClient(this@SplashActivity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(this@SplashActivity){it->
            it.locationSettingsStates
            Handler().postDelayed({

                //  if(prefManager!!.isValid().equals("true")){

                //   startActivity(Intent(this, DashBoard::class.java))
                // }else{
                startActivity(Intent(this, LoginActivity::class.java))
                //    }

            },2000)
        }

        task.addOnFailureListener(this@SplashActivity) { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // grantLocPer()

                    // grantLocPer()
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult (this@SplashActivity, REQUEST_CHECK_SETTINGS)

                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }

            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return !(ActivityCompat.checkSelfPermission(this@SplashActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@SplashActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            checkLocationService()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                val alertDialog2 = AlertDialog.Builder(
                    this
                )
                alertDialog2.setTitle("Alert...")
                alertDialog2.setMessage("Permission required!!")
                alertDialog2.setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface?, which: Int ->
                    ActivityCompat.requestPermissions(
                        this@SplashActivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

                }
                alertDialog2.setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, which: Int -> dialog.cancel()

                }
                alertDialog2.show()


                /*             val dialog = Dialog(this@SplashActivity)
                             dialog.setCancelable(false)
                             dialog.setContentView(R.layout.permission_layout)
                             val body = dialog.findViewById(R.id.error) as TextView
                             body.text = "Warning! We are unable to continue if you want then allow permission this time otherwise you have give app permission manually"
                             // body.text = title
                             val yesBtn = dialog.findViewById(R.id.ok) as Button
                             yesBtn.setOnClickListener {
                                 dialog.dismiss()
             
             
                             }
             
                             dialog.show()*/

            }
        }
    }
    override fun onResume() {
        super.onResume()
        /*  registerBroadcast()
          getStart()

          checkDownloadPermission()*/
        var prefManager = PrefManager(this)
        prefManager.setType("")
        prefManager.setToLatL("")
        prefManager.setToLngL("")
        prefManager.setToLatM("")
        prefManager.setToLngM("")
        prefManager.setTypeC("")
        prefManager.setToLatLC("")
        prefManager.setToLngLC("")
        prefManager.setToLatMC("")
        prefManager.setToLngMC("")

    }
}