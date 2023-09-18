package com.example.sapodemo.data.network.model.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderResponse(
    @Json(name = "order")
    val orderPost: OrderPost?
)