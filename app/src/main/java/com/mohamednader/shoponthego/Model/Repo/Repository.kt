package com.mohamednader.shoponthego.Model.Repo

import android.util.Log
import com.example.example.Customerre
import com.example.example.PostCustomer
import com.example.example.SingleProduct
import com.mohamednader.shoponthego.Database.LocalSource
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.SmartCollection
import com.mohamednader.shoponthego.Model.Pojo.customer.Customer
import com.mohamednader.shoponthego.Network.RemoteSource
import com.mohamednader.shoponthego.SharedPrefs.SharedPrefsSource
import kotlinx.coroutines.flow.Flow

class Repository constructor(
    remoteSource: RemoteSource, localSource: LocalSource, sharedPrefsSource: SharedPrefsSource
) : RepositoryInterface {

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
        fun getInstance(
            remoteSource: RemoteSource,
            localSource: LocalSource,
            sharedPrefsSource: SharedPrefsSource
        ): Repository {
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

    override suspend fun getAllBrands(): Flow<List<SmartCollection>> {
        Log.i(TAG, "getAllBrands: REPO")
        return remoteSource.getAllBrands()
    }

    override suspend fun getAllProductBrands(id: String): Flow<List<Product>> {
        Log.i(TAG, "getAllBrandsProducts: REPO")
        return remoteSource.getAllProductBrands(id)
    }

    override suspend fun getProductWithId(id: String): Flow<SingleProduct> {
        Log.i(TAG, "getProductWithId: REPO")
        return remoteSource.getProductWithId(id)
    }

    override suspend fun createCustomer(customer: PostCustomer): Flow<Customerre> {
return remoteSource.createCustomer(customer)   }


}