package com.example.sapodemo.data.network.api

import com.example.sapodemo.data.network.services.OrderApiService
object OrderApi {
    val API: OrderApiService by lazy {
        BaseApi.RETROFIT.create(OrderApiService::class.java)
    }
}