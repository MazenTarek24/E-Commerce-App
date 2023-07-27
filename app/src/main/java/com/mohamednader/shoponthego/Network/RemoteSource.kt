package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Order.Order
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>>
    suspend fun getAllPriceRules(): Flow<List<PriceRules>>

    suspend fun modifyDraftforfav(draftorder: SingleDraftOrderResponse, id: Long): Flow<DraftOrder>

    suspend fun createDraftforfav(draftorder: SingleDraftOrderResponse): Flow<DraftOrder>
    suspend fun getAllBrands(): Flow<List<SmartCollection>>

    suspend fun getAllProductBrands(id: String): Flow<List<Product>>
    suspend fun getProductWithId(id: String): Flow<Product>
    suspend fun getDraftWithId(id: Long): Flow<DraftOrder>

    suspend fun createCustomer(customer: SingleCustomerResponse): Flow<Customer>

    suspend fun getAllProductCategory(collectionId: Long, productType: String): Flow<List<Product>>

//    suspend fun getAllProductCategoryByType( collectionId : Long,productType : String )
//            : Flow<List<Product>>

    //Currency
    suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>>
    suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>>

    suspend fun getAllOrders(): Flow<List<Order>>

    //Draft Orders
    suspend fun getAllDraftOrders(): Flow<List<DraftOrder>>
    suspend fun updateDraftOrder(draftOrderId: Long,
                                 updatedDraftOrder: SingleDraftOrderResponse): Flow<DraftOrder>

    suspend fun addDraftOrder(newDraftOrder: SingleDraftOrderResponse): Flow<DraftOrder>

    suspend fun getProductByID(productId: Long): Flow<Product>

    //Customers
    suspend fun getAllCustomers(): Flow<List<Customer>>
    suspend fun getCustomerByID(customerId: Long): Flow<Customer>
    suspend fun updateCustomer(customerId: Long,
                               updatedCustomer: SingleCustomerResponse): Flow<Customer>

    suspend fun deleteUserAddress(customerId: Long, addressId: Long)

}