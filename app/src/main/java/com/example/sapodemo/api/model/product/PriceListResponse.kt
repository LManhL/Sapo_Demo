package com.example.sapodemo.api.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PriceListResponse(
    @Json(name = "code")
    val code: String?,
    @Json(name = "created_on")
    val createdOn: String?,
    @Json(name = "currency_id")
    val currencyId: Int?,
    @Json(name = "currency_iso")
    val currencyIso: String?,
    @Json(name = "currency_symbol")
    val currencySymbol: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "init")
    val `init`: Boolean?,
    @Json(name = "is_cost")
    val isCost: Boolean?,
    @Json(name = "modified_on")
    val modifiedOn: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "tenant_id")
    val tenantId: Int?
)