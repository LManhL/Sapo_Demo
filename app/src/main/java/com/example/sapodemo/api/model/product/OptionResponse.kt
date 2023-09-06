package com.example.sapodemo.api.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OptionResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "position")
    val position: Int?,
    @Json(name = "values")
    val values: List<String>?
)