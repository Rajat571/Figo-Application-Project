package com.pearl.figgodriver


import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.pearl.figgodriver.Fragment.WaitingRegistration
import com.pearlorganisation.PrefManager
import com.pearlorganisation.figgo.UI.Fragments.MPinGenerate
import kotlinx.android.synthetic.main.active_ride_layout.*


class WelcomeActivity : AppCompatActivity() {
    lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val window=window
        var welcome = findViewById<ConstraintLayout>(R.id.welcome_Layout)
        var waiting = findViewById<FrameLayout>(R.id.waiting_welcome)
        welcome.visibility = View.VISIBLE
        waiting.visibility = View.GONE
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        prefManager= PrefManager(this)
        Handler().postDelayed({

            if(prefManager.getRegistrationToken().equals("Done")){
                welcome.visibility = View.GONE
               waiting.visibility = View.VISIBLE
               supportFragmentManager.beginTransaction().replace(R.id.waiting_welcome,WaitingRegistration()).commit()
            //startActivity(Intent(this,DriverDashBoard::class.java))
            }else if (prefManager.getToken().equals("") || prefManager.getToken().equals("null")||prefManager.getMpin().equals("")){
            startActivity(Intent(this,LoginActivity::class.java))
        }


        },2000)
      /*  var next_btn=findViewById<TextView>(R.id.next_button)
        next_btn.setOnClickListener {
            startActivity(Intent(this,ChooseUserActivity::class.java))
        }*/
    }
}

