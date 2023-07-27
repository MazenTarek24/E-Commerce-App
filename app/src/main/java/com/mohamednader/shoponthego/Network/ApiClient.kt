package com.mohamednader.shoponthego.Network

import android.util.Log
import com.example.example.*
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.Customer
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import okhttp3.Credentials

class ApiClient : RemoteSource {

    private val TAG = "ApiClient_INFO_TAG"

    val apiService: ApiService by lazy {
        RetrofitHelper.getInstance(Constants.shopifyBaseUrl).create(ApiService::class.java)
    }

    val apiServiceForCurrency: ApiService by lazy {
        RetrofitHelper.getInstance(Constants.currencyBaseUrl).create(ApiService::class.java)
    }

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                val temp = ApiClient()
                instance = temp
                temp
            }
        }
    }

    override suspend fun getAllProducts(): Flow<List<Product>> {
        val response = apiService.getAllProducts()
        val productsList: Flow<List<Product>>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            productsList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            productsList = emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return productsList
    }

    override suspend fun getAllBrands(): Flow<List<SmartCollection>> {
        val response = apiService.getAllBrands()
        val brandList: Flow<List<SmartCollection>>
        Log.i(TAG, "getAllBrands: API-Client")
        if (response.isSuccessful) {
            brandList = flowOf(response.body()!!.smart_collections)
            Log.i(TAG, "getAllBrands: Done")

        } else {
            brandList = emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")

        }
        return brandList
    }

    override suspend fun getAllProductBrands(id: String): Flow<List<Product>> {
        val response = apiService.getAllBrandsProduct(id)
        val productList: Flow<List<Product>>
        Log.i(TAG, "getAllBrandsProducts API-Client")
        if (response.isSuccessful) {
            productList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllBrandsProducts: Done")

        } else {
            productList = emptyFlow()
            Log.i(TAG, "getAllBrandsProducts: ${response.errorBody().toString()}")
        }
        return productList
    }

    override suspend fun getProductWithId(id: String): Flow<SingleProduct> {
        val response = apiService.getProductWithId(id)
        val product: Flow<SingleProduct>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            product = flowOf(response.body()!!.product)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            return emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return product
    }

    override suspend fun getDraftWithId(id: Long): Flow<DraftOrder> {
        val response = apiService.getDraftWithId(id)
        val draftOrder: Flow<DraftOrder>
        Log.i(TAG, "getDraftwith idAPI-Client")
        if (response.isSuccessful) {
            draftOrder = flowOf(response.body()!!.draft_order)
            Log.i(TAG, "getDraftwith id: Done")

        } else {
            draftOrder = emptyFlow()
            Log.i(TAG, "getDraftwith id: ${response.errorBody().toString()}")
        }
        return draftOrder
    }

    override suspend fun createCustomer(customer: PostCustomer): Flow<Customerre> {
        val response = apiService.createCustomer(customer)
        val product: Flow<Customerre>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            product = flowOf(response.body()!!.customer)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            return emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return product
    }

    override suspend fun createDraftforfav(draftorder: PostDraftOrder): Flow<ResponseDraftOrderOb> {
        val response = apiService.createDraftOrder(draftorder)
        val draftOrder: Flow<ResponseDraftOrderOb>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            draftOrder = flowOf(response.body()!!.draftOrder)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            return emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return draftOrder
    }

    override suspend fun getAllProductCategory(
            collectionId: Long,
            productType: String,
    ): Flow<List<Product>> {
        val response = apiService.getAllCategoryProduct(collectionId, productType)
        val productCategoryList: Flow<List<Product>>
        Log.i(TAG, "getAllCategoryProducts API-Client")
        if (response.isSuccessful) {
            productCategoryList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllCategoryProducts: Done")
        } else {
            productCategoryList = emptyFlow()
            Log.i(TAG, "getAllCategoryProducts: ${response.errorBody().toString()}")
        }
        return productCategoryList
    }

//    override suspend fun getAllProductCategoryByType(productType: String): Flow<List<Product>> {
//        val response = apiService.getAllCategoryProductByType(productType)
//        val productCategoryList : Flow<List<Product>>
//        Log.i(TAG, "getAllCategoryProducts API-Client")
//        if (response.isSuccessful)
//        {
//            productCategoryList = flowOf(response.body()!!.products)
//            Log.i(TAG, "getAllCategoryProducts: Done")
//        }else{
//            productCategoryList = emptyFlow()
//            Log.i(TAG, "getAllCategoryProducts: ${response.errorBody().toString()}")
//        }
//        return productCategoryList
//    }

    override suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>> {
        val response = apiServiceForCurrency.getCurrencyConvertor(
                Credentials.basic(
                        Constants.currencyApiUsername, Constants.currencyApiPassword), from, to)
        val currencyRes: Flow<List<ToCurrency>>
        Log.i(TAG, "getCurrencyConvertor API-Client")
        if (response.isSuccessful) {
            currencyRes = flowOf(response.body()!!.to)
            Log.i(TAG, "getCurrencyConvertor: Done")

        } else {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "getCurrencyConvertor: Error: $errorBody")
            currencyRes = emptyFlow()
        }
        return currencyRes
    }

    override suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>> {
        val response = apiServiceForCurrency.getAllCurrencies(
                Credentials.basic(
                        Constants.currencyApiUsername, Constants.currencyApiPassword))
        val currencyRes: Flow<List<CurrencyInfo>>
        Log.i(TAG, "getAllCurrencies API-Client")
        if (response.isSuccessful) {
            currencyRes = flowOf(response.body()!!.currencies)
            Log.i(TAG, "getAllCurrencies: Done")

        } else {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "getAllCurrencies: Error: $errorBody")
            currencyRes = emptyFlow()
        }
        return currencyRes
    }

    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        val response = apiService.getDiscountCodesByPriceRuleID(priceRuleId)
        val discountCodesList: Flow<List<DiscountCodes>>
        Log.i(TAG, "getDiscountCodesByPriceRuleID: API-Client")
        if (response.isSuccessful) {
            discountCodesList = flowOf(response.body()!!.discountCodesList)
            Log.i(TAG, "getDiscountCodesByPriceRuleID: Done")
        } else {
            discountCodesList = emptyFlow()
            Log.i(TAG, "getDiscountCodesByPriceRuleID: ${response.errorBody().toString()}")
        }
        return discountCodesList
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRules>> {
        val response = apiService.getAllPriceRules()
        val priceRulesList: Flow<List<PriceRules>>
        Log.i(TAG, "getAllPriceRules: API-Client")
        if (response.isSuccessful) {
            priceRulesList = flowOf(response.body()!!.priceRulesList)
            Log.i(TAG, "getAllPriceRules: Done")
        } else {
            priceRulesList = emptyFlow()
            Log.i(TAG, "getAllPriceRules: ${response.errorBody().toString()}")
        }
        return priceRulesList
    }

    override suspend fun modifyDraftforfav(
            draftorder: DraftOrderResponse,
            id: Long
    ): Flow<DraftOrdermo> {
        val response = apiService.modifyDraftOrder(id, draftorder)
        val draftOrder: Flow<DraftOrdermo>
        Log.i(TAG, "getAllBrandsProducts API-Client")
        if (response.isSuccessful) {
            draftOrder = flowOf(response.body()!!.draft_order)
            Log.i(TAG, "getAllBrandsProducts: Done")

        } else {
            draftOrder = emptyFlow()
            Log.i(TAG, "getAllBrandsProducts: ${response.errorBody().toString()}")
        }
        return draftOrder
    }

    override suspend fun getAllCustomer(): Flow<List<Customers>> {
        val response = apiService.getAllCustomer()
        val customers: Flow<List<Customers>>
        Log.i(TAG, "getAllBrandsProducts API-Client")
        if (response.isSuccessful) {
            customers = flowOf(response.body()!!.customers)
            Log.i(TAG, "getAllBrandsProducts: Done")

        } else {
            customers = emptyFlow()
            Log.i(TAG, "getAllBrandsProducts: ${response.errorBody().toString()}")
        }
        return customers
    }

    override suspend fun getAllDraftsOrders(): Flow<List<DraftOrders>> {
        val response = apiService.getAllDraftorders()
        val draftsOrders: Flow<List<DraftOrders>>
        Log.i(TAG, "getAllBrandsProducts API-Client")
        if (response.isSuccessful) {
            draftsOrders = flowOf(response.body()!!.draftOrders)
            Log.i(TAG, "getAllBrandsProducts: Done")

        } else {
            draftsOrders = emptyFlow()
            Log.i(TAG, "getAllBrandsProducts: ${response.errorBody().toString()}")
        }
        return draftsOrders
    }

    //Draft Orders
    override suspend fun getAllDraftOrders(): Flow<List<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>> {
        val response = apiService.getAllDraftOrders()
        val draftOrdersList: Flow<List<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>>
        Log.i(TAG, "getAllDraftOrders: API-Client")
        if (response.isSuccessful) {
            draftOrdersList = flowOf(response.body()!!.draftOrders)
            Log.i(TAG, "getAllDraftOrders: Done")
        } else {
            draftOrdersList = emptyFlow()
            Log.i(TAG, "getAllDraftOrders: ${response.errorBody().toString()}")
        }
        return draftOrdersList
    }

    override suspend fun updateDraftOrder(
            draftOrderId: Long,
            updatedDraftOrder: SingleDraftOrderResponse
    ): Flow<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder> {
        val response = apiService.updateDraftOrder(draftOrderId, updatedDraftOrder)
        val draftOrder: Flow<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>
        Log.i(TAG, "updateDraftOrder: API-Client")
        if (response.isSuccessful) {
            draftOrder = flowOf(response.body()!!.draftOrder)
            Log.i(TAG, "updateDraftOrder: Done")
        } else {
            draftOrder = emptyFlow()
            Log.i(TAG, "updateDraftOrder: ${response.errorBody().toString()}")
            Log.i(TAG, "updateDraftOrder: ${response.errorBody()?.string()}")
            Log.i(TAG, "updateDraftOrder: ${response.code()}")
            Log.i(TAG, "updateDraftOrder: ${response.message()}")
        }
        return draftOrder
    }

    override suspend fun addDraftOrder(newDraftOrder: SingleDraftOrderResponse): Flow<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder> {
        val response = apiService.addDraftOrder(newDraftOrder)
        val draftOrder: Flow<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>
        Log.i(TAG, "addDraftOrder: API-Client")
        if (response.isSuccessful) {
            draftOrder = flowOf(response.body()!!.draftOrder)
            Log.i(TAG, "addDraftOrder: Done")
        } else {
            draftOrder = emptyFlow()
            Log.i(TAG, "addDraftOrder: ${response.errorBody().toString()}")
        }
        return draftOrder

    }

    //Customers
    override suspend fun getAllCustomers(): Flow<List<Customer>> {
        val response = apiService.getAllCustomers()
        val customersList: Flow<List<Customer>>
        Log.i(TAG, "getAllCustomers: API-Client")
        if (response.isSuccessful) {
            customersList = flowOf(response.body()!!.customers)
            Log.i(TAG, "getAllCustomers: Done")
        } else {
            customersList = emptyFlow()
            Log.i(TAG, "getAllCustomers: ${response.errorBody().toString()}")
        }
        return customersList
    }

    override suspend fun getCustomerByID(customerId: Long): Flow<Customer> {
        val response = apiService.getCustomerByID(customerId)
        val customer: Flow<Customer>
        Log.i(TAG, "getCustomerByID: API-Client")
        if (response.isSuccessful) {
            customer = flowOf(response.body()!!.customer)
            Log.i(TAG, "getCustomerByID: Done")
        } else {
            customer = emptyFlow()
            Log.i(TAG, "getCustomerByID: ${response.errorBody().toString()}")
        }
        return customer
    }

    override suspend fun updateCustomer(customerId: Long,
                                        updatedCustomer: SingleCustomerResponse): Flow<Customer> {
        val response = apiService.updateCustomer(customerId, updatedCustomer)
        val customer: Flow<Customer>
        Log.i(TAG, "updateCustomer: API-Client")
        if (response.isSuccessful) {
            customer = flowOf(response.body()!!.customer)
            Log.i(TAG, "updateCustomer: Done")
        } else {
            customer = emptyFlow()
            Log.i(TAG, "updateCustomer: ${response.errorBody().toString()}")
        }
        return customer

    }

    override suspend fun deleteUserAddress(customerId: Long, addressId: Long) {
        val response = apiService.deleteUserAddress(customerId, addressId)
    }

    override suspend fun getProductByID(productId: Long): Flow<Product> {
        val response = apiService.getProductByID(productId)
        val product: Flow<Product>
        Log.i(TAG, "getProductByID: API-Client")
        if (response.isSuccessful) {
            product = flowOf(response.body()!!.product)
            Log.i(TAG, "getProductByID: Done")
        } else {
            product = emptyFlow()
            Log.i(TAG, "getProductByID: ${response.errorBody().toString()}")
        }
        return product
    }

}