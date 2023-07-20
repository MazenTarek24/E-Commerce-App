package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getAllProducts(): Flow<List<Product>>

    suspend fun getAllBrands() : Flow<List<SmartCollection>>
}