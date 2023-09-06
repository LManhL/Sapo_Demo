package com.example.sapodemo.api.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InventoryResponse(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "available")
    val available: Double?,
    @Json(name = "bin_location")
    val binLocation: String?,
    @Json(name = "committed")
    val committed: Double?,
    @Json(name = "incoming")
    val incoming: Double?,
    @Json(name = "location_id")
    val locationId: Int?,
    @Json(name = "mac")
    val mac: Double?,
    @Json(name = "max_value")
    val maxValue: Double?,
    @Json(name = "min_value")
    val minValue: Double?,
    @Json(name = "modified_on")
    val modifiedOn: Any?,
    @Json(name = "on_hand")
    val onHand: Double?,
    @Json(name = "onway")
    val onway: Double?,
    @Json(name = "variant_id")
    val variantId: Int?,
    @Json(name = "wait_to_pack")
    val waitToPack: Double?
)