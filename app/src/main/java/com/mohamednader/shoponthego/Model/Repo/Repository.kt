package com.mohamednader.shoponthego.Model.Repo

import android.util.Log
import com.example.example.*
import com.mohamednader.shoponthego.Database.LocalSource
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrder
import com.mohamednader.shoponthego.Model.Pojo.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Network.RemoteSource
import com.mohamednader.shoponthego.SharedPrefs.SharedPrefsSource
import kotlinx.coroutines.flow.Flow

class Repository constructor(remoteSource: RemoteSource,
                             localSource: LocalSource,
                             sharedPrefsSource: SharedPrefsSource) : RepositoryInterface {

    private val TAG = "Repository_INFO_TAG"

    private val remoteSource: RemoteSource
    private val localSource: LocalSource
    private val sharedPrefsSource: SharedPrefsSource

    init {
        this.remoteSource = remoteSource
        this.localSource = localSource
        this.sharedPrefsSource = sharedPrefsSource
    }

    companion object {
        private var repo: Repository? = null
        fun getInstance(remoteSource: RemoteSource,
                        localSource: LocalSource,
                        sharedPrefsSource: SharedPrefsSource): Repository {
            return repo ?: synchronized(this) {
                val instance = Repository(remoteSource, localSource, sharedPrefsSource)
                repo = instance
                instance
            }
        }
    }

    override suspend fun getAllProducts(): Flow<List<Product>> {
        Log.i(TAG, "getAllProducts: REPO")
        return remoteSource.getAllProducts()
    }

    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        Log.i(TAG, "getDiscountCodesByPriceRuleID: REPO")
        return remoteSource.getDiscountCodesByPriceRuleID(priceRuleId)
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRules>> {
        Log.i(TAG, "getAllPriceRules: REPO")
        return remoteSource.getAllPriceRules()
    }

    override suspend fun modifyDraftforfav(draftorder: DraftOrderResponse,
                                           id: Long): Flow<DraftOrdermo> {
        return remoteSource.modifyDraftforfav(draftorder, id)
    }

    override suspend fun createDraftforfav(draftorder: PostDraftOrder): Flow<ResponseDraftOrderOb> {
        return remoteSource.createDraftforfav(draftorder)
    }

    override suspend fun getAllBrands(): Flow<List<SmartCollection>> {
        Log.i(TAG, "getAllBrands: REPO")
        return remoteSource.getAllBrands()
    }

    override suspend fun getAllProductBrands(id: String): Flow<List<Product>> {
        Log.i(TAG, "getAllBrandsProducts: REPO")
        return remoteSource.getAllProductBrands(id)
    }

    override suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>> {
        return remoteSource.getCurrencyConvertor(from, to)
    }

    override suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>> {
        return remoteSource.getAllCurrencies()
    }

    override suspend fun getProductWithId(id: String): Flow<SingleProduct> {
        Log.i(TAG, "getProductWithId: REPO")
        return remoteSource.getProductWithId(id)
    }

    override suspend fun getDraftWithId(id: Long): Flow<DraftOrder> {
        return remoteSource.getDraftWithId(id)
    }

    override suspend fun createCustomer(customer: PostCustomer): Flow<Customerre> {
        return remoteSource.createCustomer(customer)
    }

    override suspend fun getAllCustomer(): Flow<List<Customers>> {
        return remoteSource.getAllCustomer()
    }

    override suspend fun getAllDraftsOrders(): Flow<List<DraftOrders>> {
        return remoteSource.getAllDraftsOrders()
    }

    override suspend fun getAllProductCategory(
            collectionId: Long,
            productType: String,
    ): Flow<List<Product>> {
        Log.i(TAG, "getAllCategoryProducts: REPO")
        return remoteSource.getAllProductCategory(collectionId, productType)
    }

//    override suspend fun getAllProductCategoryByType(collectionId : Long,productType: String): Flow<List<Product>> {
//        return remoteSource.getAllProductCategoryByType(collectionId,productType)
//    }

    override suspend fun getAllDraftOrders(): Flow<List<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder>> {
        return remoteSource.getAllDraftOrders()
    }

    override suspend fun updateDraftOrder(draftOrderId: Long,
                                          updatedDraftOrder: SingleDraftOrderResponse): Flow<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder> {
        return remoteSource.updateDraftOrder(draftOrderId, updatedDraftOrder)
    }

    override suspend fun addDraftOrder(newDraftOrder: SingleDraftOrderResponse): Flow<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrder> {
        return remoteSource.addDraftOrder(newDraftOrder)
    }

    override suspend fun getProductByID(productId: Long): Flow<Product> {
        return remoteSource.getProductByID(productId)
    }

    override suspend fun getAllCustomers(): Flow<List<com.mohamednader.shoponthego.Model.Pojo.Customers.Customer>> {
        return remoteSource.getAllCustomers()
    }

    override suspend fun getCustomerByID(customerId: Long): Flow<com.mohamednader.shoponthego.Model.Pojo.Customers.Customer> {
        return remoteSource.getCustomerByID(customerId)
    }

    override suspend fun updateCustomer(customerId: Long,
                                        updatedCustomer: SingleCustomerResponse): Flow<com.mohamednader.shoponthego.Model.Pojo.Customers.Customer> {
        return remoteSource.updateCustomer(customerId, updatedCustomer)
    }

    override suspend fun deleteUserAddress(customerId: Long, addressId: Long) {
        remoteSource.deleteUserAddress(customerId, addressId)
    }

}