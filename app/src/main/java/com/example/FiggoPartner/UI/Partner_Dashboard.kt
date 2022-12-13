package com.example.FiggoPartner.UI

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.figgodriver.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Partner_Dashboard : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_dashboard)
        var window=window
        window.setStatusBarColor(Color.parseColor("#000F3B"))
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController=navHostFragment.navController
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.navigation_bar)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}