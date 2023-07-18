package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getAllProducts(): Flow<List<Product>>

}