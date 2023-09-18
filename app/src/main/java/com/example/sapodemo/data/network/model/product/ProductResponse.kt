package com.example.sapodemo.data.network.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "brand")
    val brand: Any?,
    @Json(name = "category")
    val category: String?,
    @Json(name = "category_code")
    val categoryCode: String?,
    @Json(name = "category_id")
    val categoryId: Int?,
    @Json(name = "created_on")
    val createdOn: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image_name")
    val imageName: Any?,
    @Json(name = "image_path")
    val imagePath: Any?,
    @Json(name = "images")
    val imageProductRespons: List<ImageProductResponse>?,
    @Json(name = "medicine")
    val medicine: Boolean?,
    @Json(name = "modified_on")
    val modifiedOn: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "opt1")
    val opt1: String?,
    @Json(name = "opt2")
    val opt2: String?,
    @Json(name = "opt3")
    val opt3: String?,
    @Json(name = "options")
    val optionResponses: List<OptionResponse>?,
    @Json(name = "product_medicines")
    val productMedicines: Any?,
    @Json(name = "product_type")
    val productType: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "tags")
    val tags: String?,
    @Json(name = "tenant_id")
    val tenantId: Int?,
    @Json(name = "variants")
    val variantResponses: List<VariantResponse>
)
