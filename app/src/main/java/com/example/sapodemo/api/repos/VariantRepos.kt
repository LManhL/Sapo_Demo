package com.example.sapodemo.api.repos

import com.example.sapodemo.api.services.VariantApiService

object VariantRepos {
    val API: VariantApiService by lazy {
        BaseRepos.RETROFIT.create(VariantApiService::class.java)
    }
}