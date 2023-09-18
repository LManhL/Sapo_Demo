package com.example.sapodemo.data.network.model.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonOrderSourceResponse(
    @Json(name = "metadata")
    val metadata: Metadata,
    @Json(name = "order_sources")
    val orderSourceResponses: List<OrderSourceResponse>?
)