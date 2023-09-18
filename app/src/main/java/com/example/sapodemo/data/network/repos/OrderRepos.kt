package com.example.sapodemo.data.network.repos

import com.example.sapodemo.data.network.services.OrderApiService
object OrderRepos {
    val API: OrderApiService by lazy {
        BaseRepos.RETROFIT.create(OrderApiService::class.java)
    }
}