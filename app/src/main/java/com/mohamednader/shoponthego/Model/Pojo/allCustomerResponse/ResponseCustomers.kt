package com.example.example

import com.google.gson.annotations.SerializedName


data class ResponseCustomers (

  @SerializedName("customers" ) var customers : ArrayList<Customers> = arrayListOf()

)