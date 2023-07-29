package com.mohamednader.shoponthego.model.repo

import androidx.datastore.preferences.core.Preferences
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
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response


class FakeRepo (var products:List<Product>, var brand :List<SmartCollection>,var discountCodes: List<DiscountCodes> ,var priceRules: List<PriceRules>,
var draftorders :DraftOrder, var product: Product ,var draftOrder: List<DraftOrder> ,var customer:Customer
,var customers :List<Customer> ,var orders :List<Order>,var cust: Customer
                ): RepositoryInterface {
    override suspend fun getAllProducts(): Flow<List<Product>> {
        return flow { emit(products) }     }

    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        return flow { emit(discountCodes) }
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRules>> {
        return flow { emit(priceRules) }
    }

    override suspend fun modifyDraftforfav(
        draftorder: SingleDraftOrderResponse,
        id: Long
    ): Flow<DraftOrder> {
        return flow { emit(draftorders) }
    }

    override suspend fun createDraftforfav(draftorder: SingleDraftOrderResponse): Flow<DraftOrder> {
        return flow { emit(draftorders) }
    }

    override suspend fun getAllBrands(): Flow<List<SmartCollection>> {
        return flow { emit(brand) }
    }

    override suspend fun getAllProductBrands(id: String): Flow<List<Product>> {
        return flow { emit(products) }
    }

    override suspend fun getProductWithId(id: String): Flow<Product> {
        return flow { emit(product) }
    }

    override suspend fun getDraftWithId(id: Long): Flow<DraftOrder> {
        return flow { emit(draftorders) }
    }

    override suspend fun createCustomer(customer: SingleCustomerResponse): Flow<Customer> {
        return flow { emit(cust) }
    }

    override suspend fun getAllProductCategory(
        collectionId: Long,
        productType: String
    ): Flow<List<Product>> {
        return flow { emit(products) }
    }

    override suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllOrders(): Flow<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDraftOrders(): Flow<List<DraftOrder>> {
        return flow { emit(draftOrder) }
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        updatedDraftOrder: SingleDraftOrderResponse
    ): Flow<DraftOrder> {
        return flow { emit(draftorders) }
    }

    override suspend fun addDraftOrder(newDraftOrder: SingleDraftOrderResponse): Flow<DraftOrder> {
        return flow { emit(draftorders) }
    }

    override suspend fun completeDraftOrderPaid(draftOrderId: Long): Flow<DraftOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun completeDraftOrderPending(draftOrderId: Long,
                                                   paymentPendingTRUE: Boolean): Flow<DraftOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getProductByID(productId: Long): Flow<Product> {
        return flow { emit(product) }
    }

    override suspend fun getAllCustomers(): Flow<List<Customer>> {
        return flow { emit(customers) }
    }

    override suspend fun getCustomerByID(customerId: Long): Flow<Customer> {
        return flow { emit(customer) }
    }

    override suspend fun updateCustomer(
        customerId: Long,
        updatedCustomer: SingleCustomerResponse
    ): Flow<Customer> {
        return flow { emit(customer) }
    }

    override suspend fun deleteUserAddress(customerId: Long, addressId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getOrderByID(orderId: Long): Flow<Order> {
        TODO("Not yet implemented")
    }

    override suspend fun saveStringDS(key: Preferences.Key<String>, value: String) {
        TODO("Not yet implemented")
    }

    override fun getStringDS(key: Preferences.Key<String>): Flow<String?> {
        TODO("Not yet implemented")
    }


}