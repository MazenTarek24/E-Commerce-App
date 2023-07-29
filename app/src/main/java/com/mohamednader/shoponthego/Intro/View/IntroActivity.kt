package com.mohamednader.shoponthego.Intro.View

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ActivityCartBinding
import com.mohamednader.shoponthego.databinding.ActivityIntroBinding
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit

class IntroActivity : AppCompatActivity() {

    lateinit var binding : ActivityIntroBinding
    val TAG = "Intro_INFO_TAG"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}