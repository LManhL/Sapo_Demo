package com.example.sapodemo.api.repos

import com.example.sapodemo.api.services.OrderApiService
object OrderRepos {
    val API: OrderApiService by lazy {
        BaseRepos.RETROFIT.create(OrderApiService::class.java)
    }
}