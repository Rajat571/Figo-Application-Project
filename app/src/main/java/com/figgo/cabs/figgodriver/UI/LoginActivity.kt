package com.figgo.cabs.figgodriver.UI

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.figgo.cabs.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.figgo.cabs.pearllib.BasePublic
import com.figgo.cabs.pearllib.Session


class LoginActivity : BasePublic(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks  {
    private val REQUEST_LOCATION = 1
    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var nav_controller: NavController
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBaseApcContextParent(this, this, "ChooseUserActivity", "ChooseUserActivity");
        setLayoutXml();
        session =  Session(this);
        val isConnected = isNetworkConnected(this.applicationContext)
        if (isConnected) {
           // initDrawer()
        //    cTheme
            verifyVersion()
            //setuserName()
            internetChangeBroadCast()
            printLogs(LogTag, "onCreate", "initConnected")
            initializeViews()
            initializeInputs()
            initializeClickListners()
            initializeLabels()
          /*  home_refresh.setOnRefreshListener(object : OnRefreshListener() {
                fun onRefresh() {
                    home_refresh.setRefreshing(true)
                    //loader.setVisibility(View.VISIBLE);
                    startActivity(Intent(this@UDashboard, UDashboard::class.java))
                    finish()
                }
            })*/
        }
        else {
            val alertDialog2: AlertDialog.Builder = AlertDialog.Builder(
                this@LoginActivity
            )
            alertDialog2.setTitle("No Internet Connection")
            alertDialog2.setPositiveButton("Try Again",
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = intent
                    finish()
                    startActivity(intent)
                })
            alertDialog2.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    finishAffinity()
                    System.exit(0)
                })
            alertDialog2.setCancelable(false)
            alertDialog2.show()
        }
        actionBar?.hide()
var permissionCheck:Boolean= true
        var i=0
        var window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_LOCATION)
/*
while (i<2) {
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
               i+=1

            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                i+=1

            }

            else -> {
                // No location access granted.
                permissionCheck = true
            }
        }
    }

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
    locationPermissionRequest.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,

        )
    )
}
        var j=0
        while (j<1) {

            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false) -> {
                        j+=1
                    }
                    else -> {
                        // No location access granted.
                        permissionCheck = true
                    }
                }
            }

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

                    )
            )
        }
*/

        mGoogleApiClient =
            GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build()
        var nav_host_fragment=supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        nav_controller=nav_host_fragment.navController

    }
    override fun setLayoutXml() {
        setContentView(R.layout.activity_login)
    }

    override fun initializeViews() {

    }

    override fun initializeClickListners() {

    }

    override fun initializeInputs() {

    }

    override fun initializeLabels() {

    }
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }
}