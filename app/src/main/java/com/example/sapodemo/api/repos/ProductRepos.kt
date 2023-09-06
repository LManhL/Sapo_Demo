package com.example.sapodemo.api.repos

import com.example.sapodemo.api.services.ProductApiService

object ProductRepos{
    val API: ProductApiService by lazy {
        BaseRepos.RETROFIT.create(ProductApiService::class.java)
    }
}