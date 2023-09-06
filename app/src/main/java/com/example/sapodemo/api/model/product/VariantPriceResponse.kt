package com.example.sapodemo.api.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VariantPriceResponse(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "included_tax_price")
    val includedTaxPrice: Double?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "price_list")
    val priceListResponse: PriceListResponse?,
    @Json(name = "price_list_id")
    val priceListId: Int?,
    @Json(name = "value")
    val value: Double?
)