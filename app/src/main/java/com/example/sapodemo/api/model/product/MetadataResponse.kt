package com.example.sapodemo.api.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetadataResponse(
    @Json(name = "limit")
    val limit: Int,
    @Json(name = "page")
    val page: Int,
    @Json(name = "total")
    val total: Int
)