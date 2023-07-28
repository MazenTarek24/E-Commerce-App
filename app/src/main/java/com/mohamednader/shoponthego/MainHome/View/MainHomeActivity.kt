package com.mohamednader.shoponthego.MainHome.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mohamednader.shoponthego.BuildConfig
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ActivityMainHomeBinding
import timber.log.Timber


class MainHomeActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var binding: ActivityMainHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val navHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavView =findViewById<BottomNavigationView>(R.id.bottom_navigation)

        setupWithNavController(bottomNavView, navController)

    }
}

