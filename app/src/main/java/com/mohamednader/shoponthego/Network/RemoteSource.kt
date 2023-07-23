package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>>
    suspend fun getAllPriceRules(): Flow<List<PriceRules>>



    suspend fun getAllBrands() : Flow<List<SmartCollection>>

    suspend fun getAllProductBrands(id : String) : Flow<List<Product>>

    suspend fun getAllProductCategory(collectionId : String , productType : String , vendor : String)
    : Flow<List<Product>>


}