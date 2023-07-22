package com.mohamednader.shoponthego.Model.Pojo.customer

import com.example.example.Addresses
import com.google.gson.annotations.SerializedName

data class Customer (

    @SerializedName("first_name"     ) var firstName     : String?              = null,
    @SerializedName("last_name"      ) var lastName      : String?              = null,
    @SerializedName("email"          ) var email         : String?              = null,
    @SerializedName("phone"          ) var phone         : String?              = null,
    @SerializedName("verified_email" ) var verifiedEmail : Boolean?             = null,
    @SerializedName("addresses"      ) var addresses     : ArrayList<Addresses> = arrayListOf()

)