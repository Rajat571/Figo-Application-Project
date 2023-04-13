package com.figgo.cabs.figgodriver.UI

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.pearllib.GlobalVariables
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import java.io.File


class SplashActivity : AppCompatActivity() {

    private val REQUEST_LOCATION = 1
    private lateinit var locationRequest: LocationRequest
    var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=101
    lateinit var prefManager: PrefManager
    private val REQUEST_CHECK_SETTINGS: Int=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
prefManager = PrefManager(this)
        var become_the_luxury=findViewById<TextView>(R.id.become_the_luxury)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_side)
        become_the_luxury.startAnimation(slideAnimation)
        grantLocPer()


    }

    private fun permissionSettingDialog() {
        val alertDialog = AlertDialog.Builder(
            this
        )
        alertDialog.setTitle("Alert...")
        alertDialog.setMessage("Please location permission is required for the working of this app. Please turn on the location from settings. ")
        alertDialog.setPositiveButton("Yes"){
            dialog:DialogInterface?,which:Int->
            run {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                GlobalVariables.goneToSettings=true

            }
        }
        alertDialog.show()
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
                prefManager.setPermissionDeniedCount(0)
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
                    prefManager.setPermissionDeniedCount(1)
                    if(prefManager.getPermissionDeniedCount()>=1){
                        permissionSettingDialog()
                    }
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


        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                    run {
                        ActivityCompat.requestPermissions(
                            this@SplashActivity,
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                        )
                        prefManager.setPermissionDeniedCount(1)
                        if(prefManager.getPermissionDeniedCount()>=1){
                            permissionSettingDialog()
                        }
                    }

                }
                alertDialog2.setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, which: Int -> run {
                    dialog.cancel()
                    if (prefManager.getPermissionDeniedCount() >= 1) {
                        permissionSettingDialog()
                    }
                }
                    //clearStorage()

alertDialog2.show()
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

        if(GlobalVariables.goneToSettings) {
            var intent = Intent(this, SplashActivity::class.java)
            finish()
            GlobalVariables.goneToSettings=false
            startActivity(intent)
        }
/*
        val intent = Intent(this,SplashActivity::class.java)
        finish()
        startActivity(intent)*/

    }

    @SuppressLint("ServiceCast")
    fun clearStorage() {
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            (this.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .clearApplicationUserData() // note: it has a return value!
        } else {
            // use old hacky way, which can be removed
            // once minSdkVersion goes above 19 in a few years.
        }
    }

    fun clearApplicationData() {
        val cache = cacheDir
        val appDir = File(cache.parent)
        if (appDir.exists()) {
            val children = appDir.list()
            for (s in children) {
                if (s != "lib") {
                    deleteDir(File(appDir, s))
                    Log.i(
                        "TAG",
                        "**************** File /data/data/APP_PACKAGE/$s DELETED *******************"
                    )
                }
            }
        }
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

}