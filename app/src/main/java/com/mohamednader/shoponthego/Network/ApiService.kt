package com.mohamednader.shoponthego.Network

import com.example.example.*
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodesResponse
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRulesResponse
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ConvertCurrencyResponse
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyResponse
import com.mohamednader.shoponthego.Model.Pojo.Customers.CustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.Customers.SingleCustomerResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.DraftOrders.SingleDraftOrderResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.ProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.SingleProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.BrandResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductResponse>

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

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllCategoryProductByType(@Query("product_type") productType: String = ""): Response<ProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules/{priceRuleId}/discount_codes.json")
    suspend fun getDiscountCodesByPriceRuleID(@Path("priceRuleId") priceRuleId: Long): Response<DiscountCodesResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules.json")
    suspend fun getAllPriceRules(): Response<PriceRulesResponse>

    //Currency
    @GET("convert_from.json")
    suspend fun getCurrencyConvertor(@Header("Authorization") authHeader: String,
                                     @Query("from") from: String,
                                     @Query("to") to: String): Response<ConvertCurrencyResponse>

    @GET("currencies.json")
    suspend fun getAllCurrencies(@Header("Authorization") authHeader: String): Response<CurrencyResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products/" + "{id}" + ".json")
    suspend fun getProductWithId(@Path("id") id: String): Response<ResponseProductId>

    @Headers("X-Shopify-Access-Token: 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")
    @POST("customers.json")
    suspend fun createCustomer(@Body customer: PostCustomer): Response<ResponseCustomer>

    @Headers("X-Shopify-Access-Token: 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")
    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftorder: PostDraftOrder): Response<ResponseDraftOrder>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("smart_collections.json")
    suspend fun getAllCustomer(): Response<ResponseCustomers>

    @Headers("X-Shopify-Access-Token: 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")
    @PUT("draft_orders/" + "{id}" + ".json")
    suspend fun modifyDraftOrder(@Path("id") id: Long,
                                 @Body draftorder: DraftOrderResponse): Response<ExampleJson2KtKotlinmo>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("draft_orders.json")
    suspend fun getAllDraftorders(): Response<ResponseDraftsOrders>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("draft_orders/{draft_order_id}.json")
    suspend fun getDraftWithId(@Path(value = "draft_order_id") draftOrderId: Long): Response<DraftOrderResponse>

    //Draft Orders
    @Headers("X-Shopify-Access-Token: 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")
    @GET("draft_orders.json")
    suspend fun getAllDraftOrders(): Response<com.mohamednader.shoponthego.Model.Pojo.DraftOrders.DraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @PUT("draft_orders/{id}.json")
    suspend fun updateDraftOrder(@Path("id") draftOrderId: Long,
                                 @Body updatedDraftOrder: SingleDraftOrderResponse): Response<SingleDraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @POST("draft_orders.json")
    suspend fun addDraftOrder(@Body newDraftOrder: SingleDraftOrderResponse): Response<SingleDraftOrderResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products/{id}.json")
    suspend fun getProductByID(@Path("id") productId: Long): Response<SingleProductResponse>

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

}