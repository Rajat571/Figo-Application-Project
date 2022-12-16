package com.pearl.figgodriver

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        Handler().postDelayed({
            startActivity(Intent(this,ChooseUserActivity::class.java))
        },2000)
      /*  var next_btn=findViewById<TextView>(R.id.next_button)
        next_btn.setOnClickListener {
            startActivity(Intent(this,ChooseUserActivity::class.java))
        }*/
    }
}