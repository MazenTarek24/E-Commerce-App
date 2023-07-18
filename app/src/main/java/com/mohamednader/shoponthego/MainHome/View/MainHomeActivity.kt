package com.mohamednader.shoponthego.MainHome.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mohamednader.shoponthego.Home.View.HomeFragment
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ActivityMainHomeBinding

class MainHomeActivity : AppCompatActivity() {

    private val TAG = "MainHome_INFO_TAG"
    private lateinit var binding: ActivityMainHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment())
            .commit()

    }
}