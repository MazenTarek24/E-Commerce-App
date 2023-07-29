package com.mohamednader.shoponthego.Splash.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
import kotlin.math.log

class SplashActivity : AppCompatActivity() {

    //    private lateinit var cd: CheckInternetConnection
    val TAG = "SplashActivity_INFO_TAG"

    private lateinit var mAuth: FirebaseAuth

    //View Model Members
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var factory: GenericViewModelFactory

    var firstTime: Boolean = true

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

            launch {
                splashViewModel.getStringDS(Constants.firstTimeKey).collect() { result ->
                    Log.i(TAG, "splashTimer: $result")
                    when (result) {
                        "true" -> {
                            delay(3000)
                            splashViewModel.saveStringDS(Constants.currencyKey , "EGP")
                            splashViewModel.saveStringDS(Constants.rateKey , "1.0")
                            splashViewModel.saveStringDS(Constants.firstTimeKey , "false")
                            checkUser()
                         }
                        "false" -> {
                            //sad
                            delay(3000)
                            checkUser()
                         }
                        null ->{
                            Log.i(TAG, "splashTimer: ")
                            splashViewModel.saveStringDS(Constants.firstTimeKey , "true")
                            splashViewModel.saveStringDS(Constants.currencyKey , "EGP")
                            splashViewModel.saveStringDS(Constants.rateKey , "1.0")
                            checkUser()
                        }
                    }
                }
            }


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