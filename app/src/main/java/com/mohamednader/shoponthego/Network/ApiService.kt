package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodesResponse
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRulesResponse
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ConvertCurrencyResponse
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyResponse
import com.mohamednader.shoponthego.Model.Pojo.Customers.CustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Order.OrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Order.SingleOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.ProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.SingleProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.BrandResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //Brands # Categories
    @Headers("X-Shopify-Access-Token: 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")
    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<BrandResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("collections/" + "{id}" + "/products.json")
    suspend fun getAllBrandsProduct(@Path("id") id: String): Response<ProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllCategoryProduct(@Query("collection_id") collectionId: Long?,
                                      @Query("product_type") productType: String = ""): Response<ProductResponse>
    //Brands # Categories

    //Price Rule
    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules/{priceRuleId}/discount_codes.json")
    suspend fun getDiscountCodesByPriceRuleID(@Path("priceRuleId") priceRuleId: Long): Response<DiscountCodesResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules.json")
    suspend fun getAllPriceRules(): Response<PriceRulesResponse>
    //Price Rule

    //Currency
    @GET("convert_from.json")
    suspend fun getCurrencyConvertor(@Header("Authorization") authHeader: String,
                                     @Query("from") from: String,
                                     @Query("to") to: String): Response<ConvertCurrencyResponse>

    @GET("currencies.json")
    suspend fun getAllCurrencies(@Header("Authorization") authHeader: String): Response<CurrencyResponse>
    //Currency

    //Products
    @Headers("X-Shopify-Access-Token: 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")
    @GET("products/" + "{id}" + ".json")
    suspend fun getProductWithId(@Path("id") id: String): Response<SingleProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products/{id}.json")
    suspend fun getProductByID(@Path("id") productId: Long): Response<SingleProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductResponse>

    //Products

    //Orders

    @Headers("X-Shopify-Access-Token: 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")
    @DELETE("draft_orders/{id}.json")
    suspend fun deleteDraftOrder(@Path("id") draftOrderId: Long): Response<SingleDraftOrderResponse>


    @Headers("X-Shopify-Access-Token: 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")
    @PUT("draft_orders/{draft_order_id}/complete.json")
    suspend fun completeDraftOrderPaid(@Path("draft_order_id") draftOrderId: Long): Response<SingleDraftOrderResponse>


    @Headers("X-Shopify-Access-Token: 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")
    @PUT("draft_orders/{draft_order_id}/complete.json")
    suspend fun completeDraftOrderPending(@Path("draft_order_id") draftOrderId: Long,
                                          @Query("payment_pending") paymentPendingTRUE: Boolean): Response<SingleDraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("orders.json")
    suspend fun getAllOrders(): Response<OrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("orders/{id}.json")
    suspend fun getOrderByID(@Path("id") orderId: Long): Response<SingleOrderResponse>

    //Orders

    //Draft Orders
    @Headers("X-Shopify-Access-Token: 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")
    @GET("draft_orders.json")
    suspend fun getAllDraftOrders(): Response<DraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @PUT("draft_orders/{id}.json")
    suspend fun updateDraftOrder(@Path("id") draftOrderId: Long,
                                 @Body updatedDraftOrder: SingleDraftOrderResponse): Response<SingleDraftOrderResponse>



    @Headers("X-Shopify-Access-Token: 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")
    @POST("draft_orders.json")
    suspend fun addDraftOrder(@Body newDraftOrder: SingleDraftOrderResponse): Response<SingleDraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftorder: SingleDraftOrderResponse): Response<SingleDraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @PUT("draft_orders/{id}.json")
    suspend fun modifyDraftOrder(@Path("id") id: Long,
                                 @Body draftorder: SingleDraftOrderResponse): Response<SingleDraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("draft_orders/{draft_order_id}.json")
    suspend fun getDraftWithId(@Path(value = "draft_order_id") draftOrderId: Long): Response<SingleDraftOrderResponse>

    //Draft Orders

    //Customers
    @Headers("X-Shopify-Access-Token: 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")
    @GET("customers.json")
    suspend fun getAllCustomers(): Response<CustomerResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("customers/{id}.json")
    suspend fun getCustomerByID(@Path("id") customerId: Long): Response<SingleCustomerResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @PUT("customers/{id}.json")
    suspend fun updateCustomer(@Path("id") customerId: Long,
                               @Body updatedCustomer: SingleCustomerResponse): Response<SingleCustomerResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @DELETE("customers/{customerId}/addresses/{addressId}.json")
    suspend fun deleteUserAddress(@Path("customerId") customerId: Long,
                                  @Path("addressId") addressId: Long)

    @Headers("X-Shopify-Access-Token: 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")
    @POST("customers.json")
    suspend fun createCustomer(@Body customer: SingleCustomerResponse): Response<SingleCustomerResponse>
    //Customers

}