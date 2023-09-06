package com.example.sapodemo.api.model.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonVariantResponse(
    @Json(name = "variant")
    val variantResponse: VariantResponse
)