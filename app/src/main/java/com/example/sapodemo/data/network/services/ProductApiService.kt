package com.example.sapodemo.data.network.services

import com.example.sapodemo.data.network.config.ApiQuery
import com.example.sapodemo.data.network.config.EndPoint
import com.example.sapodemo.data.network.model.product.JsonProductResponse
import com.example.sapodemo.data.network.model.product.JsonProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET(EndPoint.PRODUCT_LIST_END_POINT)
    suspend fun getProducts(@Query(ApiQuery.PAGE_QUERY) page: Int, @Query(ApiQuery.LIMIT_QUERY) limit: Int) : retrofit2.Response<JsonProductsResponse>
    @GET(EndPoint.PRODUCT_DETAIL_END_POINT)
    suspend fun getProduct(@Path(EndPoint.ID_PATH) id: Int) : retrofit2.Response<JsonProductResponse>
    @GET(EndPoint.PRODUCT_LIST_SEARCH_END_POINT)
    suspend fun searchProduct(@Query(ApiQuery.SEARCH_QUERY) query: String,
                              @Query(ApiQuery.PAGE_QUERY) page: Int,
                              @Query(ApiQuery.LIMIT_QUERY) limit: Int): retrofit2.Response<JsonProductsResponse>
}