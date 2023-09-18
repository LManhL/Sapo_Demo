package com.example.sapodemo.data.network.model.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonProductResponse(
    @Json(name = "product")
    val productResponse: ProductResponse
)