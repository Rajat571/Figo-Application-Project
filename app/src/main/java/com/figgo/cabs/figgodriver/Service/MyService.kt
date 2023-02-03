package com.figgo.cabs.figgodriver.Service

import android.Manifest
import android.app.Notification
import android.content.Intent
import android.os.CountDownTimer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*

class MyService : Service() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val REQUEST_LOCATION = 1
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lat=0.0
    var addressName:String=""
    var lon=0.0
    lateinit var geocoder: Geocoder
    lateinit var prefManager:PrefManager

    lateinit var address:List<Address>
    var bi = Intent(COUNTDOWN_BR)
    var cdt: CountDownTimer? = null
    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        /* Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent =PendingIntent.getService(getApplicationContext(), 1,restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);*/

        Log.d("logs-- ", "servive_in_bg")
        super.onTaskRemoved(rootIntent)
    }

    override fun onCreate() {
        super.onCreate()


prefManager= PrefManager(applicationContext)
        scope.launch(Dispatchers.IO) {  latlong() }

       // Toast.makeText(applicationContext,"sucess",Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1,
            Notification()
        )
    }
    private suspend fun latlong() {
        var x:Float=0.01f
        while(true){
            addressName=""

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
                return
            }
            fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                      //  Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                    else {

                        lat = location.latitude
                        prefManager.setlatitude(lat.toFloat()+x)
                        lon = location.longitude
                        prefManager.setlongitude(lon.toFloat()+x)
                        x=x+0.02f
                        geocoder = Geocoder(this, Locale.getDefault())

                        address = geocoder.getFromLocation(lat,lon,1)
                        if(address!=null){
                            addressName+=address.get(0).getAddressLine(0)
                           // Toast.makeText(this,"Address =  "+addressName,Toast.LENGTH_SHORT).show()
                        }
                        // prefManager.setlongitude(lon.toFloat())

                        val URL = "https://test.pearl-developer.com/figo/api/post-location"
                        val queue = Volley.newRequestQueue(this)
                        val json = JSONObject()
                        json.put("token", PrefManager(this).getToken())
                        json.put("lat",""+lat.toString())
                        json.put("lng",""+lon.toString())
                        json.put("location_name",""+addressName)
                        Log.d("MY_SERVICE == ","json "+json)
                        Log.d("location == ","LATLONG "+prefManager.getlatitude()+","+prefManager.getlongitude())
                        Log.d("Token == ","Token "+PrefManager(this).getToken())
                        val jsonObject=  object : JsonObjectRequest(
                            Method.POST, URL, json,
                            Response.Listener<JSONObject?> { response ->
                                Log.d("Response == ",""+response)
                                if (response != null) {
                                   // Toast.makeText(this," "+response,Toast.LENGTH_SHORT).show()
                                }
                            },{
                                Log.d("Response == ","Error "+it)
                              //  Toast.makeText(this,"Something Went Wrong !!",Toast.LENGTH_SHORT).show()
                            }){}
                        queue.add(jsonObject)
                    }
                }

            delay(20000)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "com.example.simpleapp"
        val channelName = "My Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
        Log.d("SEND DATA","Response service==="+channelName)
    }

    companion object {
        private const val TAG = "BroadcastService"
        const val COUNTDOWN_BR = "your_package_name.countdown_br"
    }
}