package com.example.sapodemo.ui.activity

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sapodemo.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frgCtnvMainNavHost) as NavHostFragment
        val navController = navHostFragment.navController
        navView = findViewById(R.id.botnavMain)
        navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController,appBarConfiguration)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.frgCtnvMainNavHost)
        return (navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp())
    }
}