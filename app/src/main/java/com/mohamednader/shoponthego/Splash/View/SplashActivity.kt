package com.mohamednader.shoponthego.Splash.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.mohamednader.shoponthego.Auth.Login.View.LoginActivity
import com.mohamednader.shoponthego.DataStore.ConcreteDataStoreSource
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.MainHome.View.MainHomeActivity
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.Splash.ViewModel.SplashViewModel
import com.mohamednader.shoponthego.Utils.Constants
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    //    private lateinit var cd: CheckInternetConnection
    private lateinit var mAuth: FirebaseAuth

    //View Model Members
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var factory: GenericViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuth = FirebaseAuth.getInstance()
//        cd = CheckInternetConnection(this)
        factory = GenericViewModelFactory(Repository.getInstance(ApiClient.getInstance(),
                ConcreteLocalSource(this@SplashActivity),
                ConcreteDataStoreSource(this@SplashActivity)))
        splashViewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)

        splashTimer()

    }

    private fun splashTimer() {
        lifecycleScope.launchWhenStarted {

            delay(1000)
            checkUser()
        }

    }

    private fun checkUser() {
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            sendUserToLogin()
        } else {
            sendUserToHome()
        }
    }

    private fun sendUserToLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

    private fun sendUserToHome() {

        val intent = Intent(this@SplashActivity, MainHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}