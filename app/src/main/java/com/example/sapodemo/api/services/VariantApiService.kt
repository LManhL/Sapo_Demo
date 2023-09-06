package com.example.sapodemo.api.services

import com.example.sapodemo.api.config.ApiQuery
import com.example.sapodemo.api.config.EndPoint
import com.example.sapodemo.api.model.product.JsonVariantResponse
import com.example.sapodemo.api.model.product.JsonVariantsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VariantApiService{
    @GET(EndPoint.VARIANT_LIST_END_POINT)
    suspend fun getVariants(@Query(ApiQuery.PAGE_QUERY) page: Int, @Query(ApiQuery.LIMIT_QUERY) limit: Int): retrofit2.Response<JsonVariantsResponse>
    @GET(EndPoint.VARIANT_DETAIL_END_POINT)
    suspend fun getVariant(@Path(EndPoint.PRODUCT_ID_PATH) productId: Int, @Path(EndPoint.VARIANT_ID_PATH) variantId: Int) : retrofit2.Response<JsonVariantResponse>
    @GET(EndPoint.VARIANT_LIST_SEARCH_END_POINT)
    suspend fun searchVariant(@Query(ApiQuery.SEARCH_QUERY) query: String, @Query(ApiQuery.PAGE_QUERY) page: Int, @Query(
        ApiQuery.LIMIT_QUERY) limit: Int): retrofit2.Response<JsonVariantsResponse>
}