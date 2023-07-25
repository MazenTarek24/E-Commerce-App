package com.mohamednader.shoponthego.Network

import com.example.example.*
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>>
    suspend fun getAllPriceRules(): Flow<List<PriceRules>>

    suspend fun modifyDraftforfav(draftorder: DraftOrderResponse,id :Long): Flow<DraftOrdermo>

    suspend fun createDraftforfav(draftorder: PostDraftOrder): Flow<ResponseDraftOrderOb>
    suspend fun getAllBrands() : Flow<List<SmartCollection>>

    suspend fun getAllProductBrands(id : String) : Flow<List<Product>>
    suspend fun getProductWithId(id : String) : Flow<SingleProduct>

    suspend fun createCustomer(customer: PostCustomer) : Flow<Customerre>
    suspend fun getAllCustomer(): Flow<List<Customers>>
    suspend fun getAllDraftsOrders(): Flow<List<DraftOrders>>


    suspend fun getAllProductCategory(collectionId : Long , productType : String )
    : Flow<List<Product>>

    suspend fun getAllProductCategoryByType( productType : String )
            : Flow<List<Product>>


    //Currency
    suspend fun getCurrencyConvertor(from: String , to: String): Flow<List<ToCurrency>>
    suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>>


}