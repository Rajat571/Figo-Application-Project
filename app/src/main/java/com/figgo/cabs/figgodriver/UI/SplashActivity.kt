package com.figgo.cabs.figgodriver.UI

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.figgo.cabs.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
           // startActivity(Intent(this,DriverDashBoard::class.java))
        },3000)
        var become_the_luxury=findViewById<TextView>(R.id.become_the_luxury)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_side)
        become_the_luxury.startAnimation(slideAnimation)
    }
}