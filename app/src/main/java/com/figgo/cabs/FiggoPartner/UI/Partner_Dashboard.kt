package com.figgo.cabs.FiggoPartner.UI

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Service.MyService
import com.figgo.cabs.figgodriver.UI.LoginActivity

import com.google.android.material.bottomnavigation.BottomNavigationView



class Partner_Dashboard : AppCompatActivity() {

    private lateinit var navController: NavController
    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_dashboard)
        var window=window
        var prefManager = PrefManager(this)
        var logout = findViewById<TextView>(R.id.partner_logout)
        logout.setOnClickListener {
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
                prefManager.setUserType("")
                prefManager.setDashboard("")
                startActivity(Intent(this, LoginActivity::class.java))
            }
            alertDialog2.setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
            alertDialog2.show()

        }
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        /*var whataspp=findViewById<ImageView>(R.id.whatsapp)
        var call=findViewById<ImageView>(R.id.call)
        var share=findViewById<ImageView>(R.id.share)

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
        }*/
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController=navHostFragment.navController
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.navigation_bar)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        var exit = findViewById<TextView>(R.id.partnerexitbutton)
        exit.setOnClickListener {
            onBackPressed()
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
}