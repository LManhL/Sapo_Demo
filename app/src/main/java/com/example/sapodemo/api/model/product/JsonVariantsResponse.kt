package com.example.sapodemo.api.model.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JsonVariantsResponse(
    @Json(name = "metadata")
     val metadataResponse: MetadataResponse,
    @Json(name = "variants")
     val variantResponses: List<VariantResponse>)