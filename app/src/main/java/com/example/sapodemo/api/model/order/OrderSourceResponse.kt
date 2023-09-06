package com.example.sapodemo.api.model.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderSourceResponse(
    @Json(name = "created_on")
    val createdOn: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "init")
    val `init`: Boolean?,
    @Json(name = "modified_on")
    val modifiedOn: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "tenant_id")
    val tenantId: Int?
)