package com.example.sapodemo.data.network.repos

import com.example.sapodemo.data.network.services.ProductApiService

object ProductRepos{
    val API: ProductApiService by lazy {
        BaseRepos.RETROFIT.create(ProductApiService::class.java)
    }
}