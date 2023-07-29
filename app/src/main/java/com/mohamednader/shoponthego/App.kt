package com.mohamednader.shoponthego

import android.app.Application
import android.util.Log
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class App : Application() {

    var clientID =
        "AU2SKtdw06mY2KKPuU_ybQrzVKWEoH4LYXQAP5QUNrZ_FXPvOhxu08LufHrgEFmWGMH2h0eHi0uj5_8-"
    var returnUrl = "com.mohamednader.shoponthego://paypalpay"

    //email : sb-gaxvm26908818@personal.example.com
    //password: O$vMt%7G
    override fun onCreate() {
        super.onCreate()

        Log.i("VIPVIP", "onCreate: ")
        val config = CheckoutConfig(application = this,
                clientId = clientID,
                environment = Environment.SANDBOX,
                returnUrl = returnUrl,
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(loggingEnabled = true))
        PayPalCheckout.setConfig(config)
    }

}