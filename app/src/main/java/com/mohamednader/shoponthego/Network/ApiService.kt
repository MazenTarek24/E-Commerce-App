package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodesResponse
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRulesResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductResponse>


    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules/{priceRuleId}/discount_codes.json")
    suspend fun getDiscountCodesByPriceRuleID(@Path("priceRuleId") priceRuleId : Long): Response<DiscountCodesResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules.json")
    suspend fun getAllPriceRules(): Response<PriceRulesResponse>

}