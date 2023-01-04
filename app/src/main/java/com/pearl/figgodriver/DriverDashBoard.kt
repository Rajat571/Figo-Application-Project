package com.pearl.figgodriver


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service.START_STICKY
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.pearl.figgodriver.Fragment.ActiveRide
import com.pearl.figgodriver.Fragment.HomeDashBoard
import com.pearl.figgodriver.Fragment.allRideRS
import com.pearl.pearllib.BaseClass
import com.pearlorganisation.PrefManager
import kotlinx.android.synthetic.main.bottom_button_layout.view.*
import kotlinx.android.synthetic.main.top_layout.view.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Runnable
import java.util.*
import java.util.concurrent.TimeUnit

class DriverDashBoard : AppCompatActivity(),CoroutineScope by MainScope() {


    var doubleBackToExitPressedOnce = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var prefManager: PrefManager
    lateinit var baseclass: BaseClass

    val scope = CoroutineScope(Job() + Dispatchers.Main)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_dash_board)


        var window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        prefManager=PrefManager(this)
        baseclass=object : BaseClass(){
            override fun setLayoutXml() {
                TODO("Not yet implemented")
            }

            override fun initializeViews() {
                TODO("Not yet implemented")
            }

            override fun initializeClickListners() {
                TODO("Not yet implemented")
            }

            override fun initializeInputs() {
                TODO("Not yet implemented")
            }

            override fun initializeLabels() {
                TODO("Not yet implemented")
            }

        }

        var whataspp=findViewById<ImageView>(R.id.whatsapp)
        var call=findViewById<ImageView>(R.id.call)
        var share=findViewById<ImageView>(R.id.share)
        var sidebar=findViewById<ImageView>(R.id.sidebar)
        var topLayout = findViewById<LinearLayout>(R.id.layout)
        var menu = topLayout.sidebar
        var off_toggle = topLayout.toggle_off
        var on_toggle = topLayout.toggle_on


      var draw_layout=findViewById<NavigationView>(R.id.draw_layout)

        var vieww= draw_layout.getHeaderView(0);
       var driverImage=vieww.findViewById<ImageView>(R.id.driverIV)
        var drivername=vieww.findViewById<TextView>(R.id.drivernamee)
        var driver_num=vieww.findViewById<TextView>(R.id.driver_numberr)
        var image =baseclass.StringToBitMap(prefManager.getDriverProfile())
        driverImage.setImageBitmap(image)
        drivername.text=prefManager.getDriverName()
        driver_num.text=prefManager.getMobileNo()


       // var back = topLayout.back_button

        var drawer = findViewById<DrawerLayout>(R.id.Dashboard_Drawer_layout)
        var action_bar_toggle = ActionBarDrawerToggle(this,drawer,R.string.nav_open, R.string.nav_close)
        drawer.addDrawerListener(action_bar_toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        menu.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    val lat = location.latitude
                    prefManager.setlatitude(lat.toFloat())
                    val lon = location.longitude
                    prefManager.setlongitude(lon.toFloat())
                  Toast.makeText(this,"Lat :"+lat+"\nLong: "+lon,Toast.LENGTH_SHORT).show()
                }

            }
scope.launch(Dispatchers.IO) {  latlong() }


        whataspp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=7505145405"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        call.setOnClickListener {
            // val callIntent = Intent(Intent.ACTION_CALL)
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:+123")
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(callIntent)
        }

        share.setOnClickListener {
            var intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"I am Inviting you to join  Figgo App for better experience to book cabs");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Invite Friends"));
        }

        sidebar.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)

        }
        topLayout.toggle_off.setOnClickListener {
            off_toggle.setBackgroundColor(Color.RED)
            on_toggle.setBackgroundColor(Color.WHITE)
            Toast.makeText(this,"off",Toast.LENGTH_SHORT).show()
        }
        topLayout.toggle_on.setOnClickListener {
            on_toggle.setBackgroundColor(Color.GREEN)
            off_toggle.setBackgroundColor(Color.WHITE)
            Toast.makeText(this,"on",Toast.LENGTH_SHORT).show()
        }
        topLayout.top_back.setOnClickListener {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }

        supportFragmentManager.beginTransaction().replace(R.id.home_frame,HomeDashBoard()).commit()
      var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->{
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame,HomeDashBoard()).commit()
                }
                R.id.call->{
                    var intent_call = Intent(Intent.ACTION_DIAL)
                    intent_call.data = Uri.parse("tel:"+"+919715597855")
                        startActivity(intent_call)
                }
                R.id.active_ride->{
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame,ActiveRide()).commit()
                }
            }
            true
        }
        draw_layout.menu.findItem(R.id.logout).setOnMenuItemClickListener {

            true
        }
        draw_layout.menu.findItem(R.id.logout).setOnMenuItemClickListener {

                val alertDialog2 = AlertDialog.Builder(
                    this
                )
                alertDialog2.setTitle("Alert...")
                alertDialog2.setMessage("Are you sure you want to exit ?")
                alertDialog2.setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface?, which: Int ->
                    Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show()
                    prefManager.setToken("")
                    prefManager.setRegistrationToken("")
                    startActivity(Intent(this,LoginActivity::class.java))
                }
                alertDialog2.setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
                alertDialog2.show()
                //       finish();

            true
        }
   }

    private suspend fun latlong() {
        while(true){

        //Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()

        //Log.d("Suspend$x",""+x.toString())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    val lat = location.latitude
                    var addressName:String=""
                    prefManager.setlatitude(lat.toFloat())
                    val lon = location.longitude
                    var geocoder:Geocoder = Geocoder(this, Locale.getDefault())
                    var address = geocoder.getFromLocation(lat,lon,1)
                    if(address!=null){
                        addressName+=address.get(0).getAddressLine(0)

                    }

                    prefManager.setlongitude(lon.toFloat())
                    Toast.makeText(this,"Address =  "+addressName,Toast.LENGTH_SHORT).show()


                    val URL = "https://test.pearl-developer.com/figo/api/post-location"
                    val queue = Volley.newRequestQueue(this)
                    val json = JSONObject()
                    json.put("token","1308|naMqaz5ThR6Bk9idH5hsxefA9gvz4kFFZ8y6Yqa6")
                    json.put("lat",""+lat.toString())
                    json.put("lng",""+lon.toString())
                    json.put("name",""+addressName)
                    val jsonObject=  object : JsonObjectRequest(
                        Method.POST, URL, json,
                        Response.Listener<JSONObject?> { response ->
                            Log.d("Response == ",""+response)
                            if (response != null) {


                            }
                        },{
                            Log.d("Response == ","Error "+it)
                            Toast.makeText(this,"Something Went Wrong !!",Toast.LENGTH_SHORT).show()
                        }){}
                    queue.add(jsonObject)
                }

            }
            delay(1000)
    }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            if (doubleBackToExitPressedOnce) {
                // System.exit(0);
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
                finish()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)
        } else {
            supportFragmentManager.popBackStack()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }



}