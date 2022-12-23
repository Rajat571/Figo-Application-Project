package com.pearl.figgodriver


import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.pearlorganisation.PrefManager
import com.pearlorganisation.figgo.UI.Fragments.MPinGenerate
import kotlinx.android.synthetic.main.active_ride_layout.*


class WelcomeActivity : AppCompatActivity() {
    lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        prefManager= PrefManager(this)
        Handler().postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
          /*  if (prefManager.getToken().equals("") || prefManager.getToken().equals("null")){

            }
            else{

            }*/

        },2000)
      /*  var next_btn=findViewById<TextView>(R.id.next_button)
        next_btn.setOnClickListener {
            startActivity(Intent(this,ChooseUserActivity::class.java))
        }*/
    }
}

