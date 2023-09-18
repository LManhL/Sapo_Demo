package com.example.sapodemo.data.network.model.product


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VariantResponse(
    @Json(name = "barcode")
    val barcode: String?,
    @Json(name = "brand_id")
    val brandId: Any?,
    @Json(name = "category_id")
    val categoryId: Int?,
    @Json(name = "composite")
    val composite: Boolean?,
    @Json(name = "composite_items")
    val compositeItems: Any?,
    @Json(name = "created_on")
    val createdOn: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "expiration_alert_time")
    val expirationAlertTime: Any?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image_id")
    val imageId: Int?,
    @Json(name = "images")
    val images: List<ImageVariantResponse>?,
    @Json(name = "init_price")
    val initPrice: Int?,
    @Json(name = "init_stock")
    val initStock: Int?,
    @Json(name = "input_vat_id")
    val inputVatId: Int?,
    @Json(name = "input_vat_rate")
    val inputVatRate: Double?,
    @Json(name = "inventories")
    val inventories: List<InventoryResponse>,
    @Json(name = "location_id")
    val locationId: Int?,
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
    @Json(name = "output_vat_id")
    val outputVatId: Double?,
    @Json(name = "output_vat_rate")
    val outputVatRate: Double?,
    @Json(name = "packsize")
    val packsize: Boolean?,
    @Json(name = "packsize_quantity")
    val packsizeQuantity: Double?,
    @Json(name = "packsize_root_id")
    val packsizeRootId: Int?,
    @Json(name = "packsize_root_name")
    val packsizeRootName: Any?,
    @Json(name = "packsize_root_sku")
    val packsizeRootSku: Any?,
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "product_name")
    val productName: String?,
    @Json(name = "product_status")
    val productStatus: Any?,
    @Json(name = "product_type")
    val productType: String?,
    @Json(name = "sellable")
    val sellable: Boolean?,
    @Json(name = "sku")
    val sku: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "tax_included")
    val taxIncluded: Boolean?,
    @Json(name = "taxable")
    val taxable: Boolean?,
    @Json(name = "tenant_id")
    val tenantId: Int?,
    @Json(name = "unit")
    val unit: String?,
    @Json(name = "variant_import_price")
    val variantImportPrice: Double?,
    @Json(name = "variant_prices")
    val variantPriceResponses: List<VariantPriceResponse>?,
    @Json(name = "variant_retail_price")
    val variantRetailPrice: Double?,
    @Json(name = "variant_whole_price")
    val variantWholePrice: Double?,
    @Json(name = "warranty")
    val warranty: Boolean?,
    @Json(name = "warranty_term_id")
    val warrantyTermId: Any?,
    @Json(name = "weight_unit")
    val weightUnit: String?,
    @Json(name = "weight_value")
    val weightValue: Double?
)