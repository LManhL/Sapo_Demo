package com.example.sapodemo.data.network.api

import com.example.sapodemo.data.network.services.ProductApiService

object ProductApi{
    val API: ProductApiService by lazy {
        BaseApi.RETROFIT.create(ProductApiService::class.java)
    }
}