package com.example.sapodemo.data.network.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonProductsResponse(
    @Json(name = "metadata")
    val metadataResponse: MetadataResponse,
    @Json(name = "products")
    val productResponses: List<ProductResponse>
)