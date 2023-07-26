package com.mohamednader.shoponthego

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class App : Application() {

    var clientID = "AbxAPOWEu8sCmJUizsb1pRfaWSLdCvYwEYNbNDYeY_v8-ZvJky3UA_10N4chH2rczde6-vaFlSgNxaT9"
    var returnUrl = "com.mohamednader.shoponthego://paypalpay"


    //email : sb-fbrmj26182867@business.example.com
    //password: g^Y3ycZV
    override fun onCreate() {
        super.onCreate()

        val config = CheckoutConfig(
                application = this,
                clientId = clientID,
                environment = Environment.SANDBOX,
                returnUrl = returnUrl,
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(
                        loggingEnabled = true
                )
        )
        PayPalCheckout.setConfig(config)
    }

}