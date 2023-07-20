package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Products.ProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.brand.BrandResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.time.ZoneId

interface ApiService {

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("smart_collections.json")
    suspend fun getAllBrands() : Response<BrandResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("collections/"+"{id}"+"/products.json")
    suspend fun getAllBrandsProduct(@Path("id") id: String) : Response<ProductResponse>

}