package com.example.sapodemo.data.network.repos

import com.example.sapodemo.data.network.services.VariantApiService

object VariantRepos {
    val API: VariantApiService by lazy {
        BaseRepos.RETROFIT.create(VariantApiService::class.java)
    }
}