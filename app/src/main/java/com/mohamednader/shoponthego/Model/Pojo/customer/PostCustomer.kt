package com.example.example

import com.google.gson.annotations.SerializedName
import com.mohamednader.shoponthego.Model.Pojo.customer.Customer


data class PostCustomer (

  @SerializedName("customer" ) var customer : Customer? = Customer()

)