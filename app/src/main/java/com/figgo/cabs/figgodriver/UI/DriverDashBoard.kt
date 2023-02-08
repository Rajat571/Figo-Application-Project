package com.figgo.cabs.figgodriver.UI


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.NotificationHomeAdapter
import com.figgo.cabs.figgodriver.Fragment.*
import com.figgo.cabs.figgodriver.Service.MyService
import com.figgo.cabs.figgodriver.model.NotificationData
import com.figgo.cabs.pearllib.BaseClass
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.bottom_button_layout.view.*
import kotlinx.android.synthetic.main.top_layout.view.*
import kotlinx.coroutines.*
import java.io.File
import java.lang.Runnable
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import java.util.*
import kotlin.properties.Delegates


class DriverDashBoard : BaseClass(),CoroutineScope by MainScope() {

    var doubleBackToExitPressedOnce = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var prefManager: PrefManager
    lateinit var baseclass: BaseClass
    lateinit var wndow:Window
    lateinit var whataspp:ImageView
    lateinit var call:ImageView
    lateinit var share:ImageView
    lateinit var sidebar:ImageView
    lateinit var topLayout:LinearLayout
    lateinit var menu:ImageView
    lateinit var off_toggle:TextView
    lateinit var on_toggle:TextView
    lateinit var draw_layout:NavigationView
    lateinit var vieww:View
    lateinit var driverImage:ImageView
    lateinit var drivername:TextView
    lateinit var driver_num:TextView
    var image: Bitmap? = null
    lateinit var drawer:DrawerLayout
    lateinit var action_bar_toggle:ActionBarDrawerToggle
    var lat by Delegates.notNull<Double>()
    var lon:Double = 0.0
    var x:Int = 1
    lateinit var url:String
    lateinit var homeFrame:FrameLayout
    var toggleState by Delegates.notNull<Boolean>()
    lateinit var offlineLayout:LinearLayout
    lateinit var bottomNav:BottomNavigationView
    lateinit var notificationlayout:LinearLayout
    lateinit var notificationRecyclerView: RecyclerView

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun setLayoutXml() {
        setContentView(R.layout.activity_driver_dash_board)
    }

    override fun initializeViews() {
        wndow=window
        prefManager= PrefManager(this)
        prefManager.setRegistrationToken("Done")
         whataspp=findViewById<ImageView>(R.id.whatsapp)
         call=findViewById<ImageView>(R.id.call)
         share=findViewById<ImageView>(R.id.share)
         sidebar=findViewById<ImageView>(R.id.sidebar)
         topLayout = findViewById<LinearLayout>(R.id.layout)
         menu = topLayout.sidebar
         off_toggle = topLayout.toggle_off
         on_toggle = topLayout.toggle_on
         draw_layout=findViewById<NavigationView>(R.id.draw_layout)
         vieww= draw_layout.getHeaderView(0);
         driverImage=vieww.findViewById<ImageView>(R.id.driverIV)
         drivername=vieww.findViewById<TextView>(R.id.drivernamee)
         driver_num=vieww.findViewById<TextView>(R.id.driver_numberr)
         driverImage.setImageBitmap(image)
        drivername.text=prefManager.getDriverName()
        driver_num.text=prefManager.getMobileNo()
        drawer = findViewById<DrawerLayout>(R.id.Dashboard_Drawer_layout)
        action_bar_toggle = ActionBarDrawerToggle(this,drawer,R.string.nav_open, R.string.nav_close)
        url = "https://api.whatsapp.com/send?phone=7505145405"
        homeFrame = findViewById<FrameLayout>(R.id.home_frame)
        toggleState=false
        offlineLayout = findViewById<LinearLayout>(R.id.offline_layout)
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        notificationlayout = findViewById<LinearLayout>(R.id.includenotifications)
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
    }

    override fun initializeClickListners() {
        var id=intent.getStringExtra("booking_id")
        Log.d("DriverDashBoard","DriverDashBoard"+id)
        drawer.addDrawerListener(action_bar_toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        image =baseclass.StringToBitMap(prefManager.getDriverProfile())
        val rootView: ViewGroup = findViewById(R.id.activeRide_layout)
        val mFade: Slide =Slide(Gravity.LEFT)
        menu.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }

        whataspp.setOnClickListener {

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
            toggleState=true
            Toast.makeText(this,"off",Toast.LENGTH_SHORT).show()
            offlineLayout.visibility=View.VISIBLE
            homeFrame.visibility=View.GONE
            stopService(Intent(this, MyService::class.java))
        }
        topLayout.notificationbell.setOnClickListener {
            val rootView: ViewGroup = findViewById(R.id.activeRide_layout)
            val mFade: Slide =Slide(Gravity.LEFT)
            if(notificationlayout.visibility==View.GONE) {

                TransitionManager.beginDelayedTransition(rootView, mFade)
                notificationlayout.visibility = View.VISIBLE
                homeFrame.visibility = View.GONE
            }
            else{
                TransitionManager.beginDelayedTransition(rootView, mFade)
            notificationlayout.visibility = View.GONE
            homeFrame.visibility=View.VISIBLE
        }
            //Toast.makeText(this,"Notification Panel working",Toast.LENGTH_SHORT).show();
        }

        topLayout.toggle_on.setOnClickListener {
            TransitionManager.beginDelayedTransition(rootView, mFade)
            on_toggle.setBackgroundColor(Color.GREEN)
            off_toggle.setBackgroundColor(Color.WHITE)
            toggleState=false
            Toast.makeText(this,"on",Toast.LENGTH_SHORT).show()
            offlineLayout.visibility=View.GONE
            homeFrame.visibility=View.VISIBLE
            startService(Intent(this, MyService::class.java))
        }
        topLayout.top_back.text="EXIT"
        topLayout.top_back.setOnClickListener {
            if(x==1) {

                topLayout.top_back.text="EXIT"
                if (toggleState==false)
                    supportFragmentManager.beginTransaction().replace(R.id.home_frame,
                        HomeDashBoard()
                    ).commit()
                else{
                    offlineLayout.visibility=View.VISIBLE
                    homeFrame.visibility=View.GONE
                }
                x=2
            }
            else{
                x=1
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(startMain)
            }
        }

       /* if (prefManager.getActiveRide()==1){
            x=1
            topLayout.top_back.text="Back"
            if (toggleState==false)
                supportFragmentManager.beginTransaction().replace(R.id.home_frame,
                    CustomerCityRideDetails()
                ).commit()
            else{
                offlineLayout.visibility=View.VISIBLE
                homeFrame.visibility=View.GONE
            }
        }
        else{
            x=2
            if (toggleState==false)
                supportFragmentManager.beginTransaction().replace(R.id.home_frame, HomeDashBoard()).commit()
            else{
                offlineLayout.visibility=View.VISIBLE
                homeFrame.visibility=View.GONE
            }
        }*/

        supportFragmentManager.beginTransaction().replace(R.id.home_frame, HomeDashBoard()).commit()
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->{
                    TransitionManager.beginDelayedTransition(rootView, mFade)
                    notificationlayout.visibility = View.GONE
                    homeFrame.visibility=View.VISIBLE
                    x=2
                    topLayout.top_back.text="EXIT"
                    if (toggleState==false)
                        supportFragmentManager.beginTransaction().replace(R.id.home_frame,
                            HomeDashBoard()
                        ).commit()
                    else{
                        offlineLayout.visibility=View.VISIBLE
                        homeFrame.visibility=View.GONE
                    }
                }
                R.id.call->{

                    var intent_call = Intent(Intent.ACTION_DIAL)
                    intent_call.data = Uri.parse("tel:"+"+919715597855")
                    startActivity(intent_call)
                }
                R.id.active_ride->{
                    x=1
                    TransitionManager.beginDelayedTransition(rootView, mFade)
                    notificationlayout.visibility = View.GONE
                    homeFrame.visibility=View.VISIBLE
                    topLayout.top_back.text="Back"
                    if (toggleState==false)
                        supportFragmentManager.beginTransaction().replace(R.id.home_frame,
                            ActiveRide()
                        ).commit()
                    else{
                        offlineLayout.visibility=View.VISIBLE
                        homeFrame.visibility=View.GONE
                    }
                }
            }
            true
        }
        var bundle:Bundle= Bundle()
        var bundle2:Bundle= Bundle()
        prefManager.setDashboard("on")
        draw_layout.menu.findItem(R.id.Support).setOnMenuItemClickListener {
            bundle.putString("Key","Support")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            drawer.closeDrawer(GravityCompat.END)
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE

            true
        }
        draw_layout.menu.findItem(R.id.About_Figgo).setOnMenuItemClickListener {
            bundle.putString("Key","About")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            drawer.closeDrawer(GravityCompat.END)
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE
            true
        }
        draw_layout.menu.findItem(R.id.term_condition).setOnMenuItemClickListener {
            bundle.putString("Key","Terms")
            var supportFrag = SupportFragment()
            drawer.closeDrawer(GravityCompat.END)
            supportFrag.arguments=bundle
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE
            true
        }
        draw_layout.menu.findItem(R.id.wallets).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,
                AccountDetailsFragment()
            ).commit()
            homeFrame.visibility=View.VISIBLE
            true
        }
        draw_layout.menu.findItem(R.id.ridehistoryDrawer).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,
               RideHistory()
            ).commit()
            homeFrame.visibility=View.VISIBLE
            true
        }
        draw_layout.menu.findItem(R.id.cancellation_policy).setOnMenuItemClickListener {
            bundle.putString("Key","Cancel")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            drawer.closeDrawer(GravityCompat.END)
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE
            true
        }
        draw_layout.menu.findItem(R.id.profile).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            bundle2.putString("Key","Profile")
           /* var supportFrag = SupportFragment()
            supportFrag.arguments=bundle*/
            var getData = GetData()
            getData.arguments=bundle2
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,getData).commit()
            homeFrame.visibility=View.VISIBLE

            true
        }
        draw_layout.menu.findItem(R.id.change_mpin_navigation).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            bundle.putString("Key","Change_Mpin")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE

            true
        }
        //deleteCache(this)
       // this.cacheDir.deleteRecursively()
        draw_layout.menu.findItem(R.id.cab_driver_details_navigation).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            bundle2.putString("Key","Cab")
            /* var supportFrag = SupportFragment()
             supportFrag.arguments=bundle*/
            var getData = GetData()
            getData.arguments=bundle2
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,getData).commit()
            homeFrame.visibility=View.VISIBLE

            true
        }
        draw_layout.menu.findItem(R.id.figgo_bussiness).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            bundle.putString("Key","Buisness")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE

            true
        }
        draw_layout.menu.findItem(R.id.saleRideExtra).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            var salesFrag = SaleExtraBooking()
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,salesFrag).commit()
            homeFrame.visibility=View.VISIBLE

            true
        }
        draw_layout.menu.findItem(R.id.Payment_History).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            bundle.putString("Key","History")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE
            true
        }
        draw_layout.menu.findItem(R.id.share_ride_Nav).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            var shareFrag = ShareRideFragment()
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,shareFrag).commit()
            homeFrame.visibility=View.VISIBLE
            Toast.makeText(this,"Ip "+myFunction(),Toast.LENGTH_SHORT).show()
            true
        }
        draw_layout.menu.findItem(R.id.share_app).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
            val appPackageName = packageName // getPackageName() from Context or Activity object




            val ip_address:String=myFunction()
            var ref_link = prefManager.getReferal();
           // sendreferal(ip_address)
        /*    val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(ref_link)
            startActivity(i)*/

            val inviteLink: String =ref_link
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I’m inviting you to join Figgo Drivers,use this code while submitting your form to earn rewards. $inviteLink")
            sendIntent.type = "text/*"
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)


            true
        }

        vieww.setOnClickListener {
            drawer.closeDrawer(GravityCompat.END)
            bundle.putString("Key","Profile")
            var supportFrag = SupportFragment()
            supportFrag.arguments=bundle
            offlineLayout.visibility=View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.home_frame,supportFrag).commit()
            homeFrame.visibility=View.VISIBLE

        }
        draw_layout.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            drawer.closeDrawer(GravityCompat.END)
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
                stopService(Intent(this, MyService::class.java))
                prefManager.setRegistrationToken("")
                startActivity(Intent(this, LoginActivity::class.java))
            }
            alertDialog2.setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
            alertDialog2.show()
            //       finish();
            true
        }
        val notificationRecycler = findViewById<RecyclerView>(R.id.notificationrecycler)
        val notificationList = kotlin.collections.ArrayList<NotificationData>()
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))
        notificationList.add(NotificationData("Congratulation, Your Profile is verified","Hello Figgo Driver, Your profile data is successfully verified.","2 hours ago"))
        notificationList.add(NotificationData("Start accepting bookings near you","Hello Figgo Driver, Your profile data is successfully verified.","1 hours ago"))

        notificationRecycler.adapter=NotificationHomeAdapter(this,notificationList)
        notificationRecycler.layoutManager=LinearLayoutManager(this)
    }



    override fun initializeInputs() {

    }

    override fun initializeLabels() {

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        setLayoutXml()
        initializeViews()
        initializeClickListners()
        initializeInputs()
        initializeLabels()
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
                    lat = location.latitude
                    prefManager.setlatitude(lat.toFloat())
                    lon = location.longitude
                    prefManager.setlongitude(lon.toFloat())
                  //Toast.makeText(this,"Lat :"+lat+"\nLong: "+lon,Toast.LENGTH_SHORT).show()
                }
            }

   }
    fun getMacAddr(): String? {
        var ip = ""
        try {
            val enumNetworkInterfaces = NetworkInterface
                .getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface = enumNetworkInterfaces
                    .nextElement()
                val enumInetAddress = networkInterface
                    .inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress = enumInetAddress.nextElement()
                    if (inetAddress.isSiteLocalAddress) {
                        ip += inetAddress.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            ip += "Something Wrong! $e\n"
        }

        return ip
    }


    private suspend fun getMyPublicIpAsync() : Deferred<String> =
        coroutineScope {
            async(Dispatchers.IO) {
                var result = ""
                result = try {
                    val url = URL("https://api.ipify.org")
                    val httpsURLConnection = url.openConnection()
                    val iStream = httpsURLConnection.getInputStream()
                    val buff = ByteArray(1024)
                    val read = iStream.read(buff)
                    String(buff,0, read)
                } catch (e: Exception) {
                    "error : $e"
                }
                return@async result
            }
        }


    private fun myFunction():String {
   /*     var ip:String=""
        CoroutineScope(Dispatchers.Main).launch {
            val myPublicIp = getMyPublicIpAsync().await()
            Toast.makeText(this@DriverDashBoard, myPublicIp, Toast.LENGTH_LONG).show()
            ip=myPublicIp.toString()

        }
        return ip.toString()*/
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (true) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase(Locale.getDefault())
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }
    private fun getIpAddress(): String? {
        var ip = ""
        try {
            val enumNetworkInterfaces = NetworkInterface
                .getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface = enumNetworkInterfaces
                    .nextElement()
                val enumInetAddress = networkInterface
                    .inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress = enumInetAddress.nextElement()
                    if (inetAddress.isSiteLocalAddress) {
                        ip += inetAddress.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            ip += "Something Wrong! $e\n"
        }
        return ip
    }
/*    fun myFunction(useIPv4: Boolean): String? {
    try {
        val en = NetworkInterface
            .getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf
                .inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                println("ip1--:$inetAddress")
                println("ip2--:" + inetAddress.hostAddress)
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet6Address) {
                    Log.d("IP address",""+NetworkInterface.getByInetAddress(inetAddress).hardwareAddress)

                    return inetAddress.getHostAddress()?.toString()
                }
            }
        }
    } catch (ex: Exception) {
        Log.e("IP Address", ex.toString())
    }
    return null
    }*/

    override fun onDestroy() {
        super.onDestroy()
        prefManager.setActiveRide(0)
    }
    private fun getLocalIpAddress(): String? {
        try {

            val wifiManager: WifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return  wifiManager.connectionInfo.macAddress
            } else {
                return "0.0.0.0"
            }

        } catch (ex: Exception) {
            Log.e("IP Address", ex.toString())
        }

        return null
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

    override fun onResume() {
        super.onResume()
        /*  registerBroadcast()
          getStart()
          checkDownloadPermission()*/
        startService(Intent(this, MyService::class.java))
        Log.i("state", "onResume")
    }

    override fun onStart() {
        super.onStart()
        /*  registerBroadcast()
          getStart()
          checkDownloadPermission()*/
        startService(Intent(this, MyService::class.java))
        Log.i("state", "onStart")
    }


    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

}