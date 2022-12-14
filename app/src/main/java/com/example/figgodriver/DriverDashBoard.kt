package com.example.figgodriver


import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.active_ride_layout.*

class DriverDashBoard : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var back:TextView
   // @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_dash_board)
        var window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController=navHostFragment.navController
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.navigation_bar)
        setupWithNavController(bottomNavigationView,navController)
//         back = findViewById<TextView>(R.id.back_button)
//
//        back.setOnClickListener {
//        finish()
//        }
    }
}