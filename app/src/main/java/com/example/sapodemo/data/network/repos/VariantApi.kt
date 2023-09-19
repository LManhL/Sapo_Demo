package com.example.sapodemo.data.network.repos

import com.example.sapodemo.data.network.services.VariantApiService

object VariantApi {
    val API: VariantApiService by lazy {
        BaseApi.RETROFIT.create(VariantApiService::class.java)
    }
}