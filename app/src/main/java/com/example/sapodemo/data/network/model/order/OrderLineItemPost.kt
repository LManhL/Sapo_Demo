package com.example.sapodemo.data.network.model.order


import com.example.sapodemo.presenter.model.OrderLineItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class OrderLineItemPost(){
    @Json(name = "barcode")
    var barcode: String? = null
    @Json(name = "composite_item_domains")
    var compositeItemDomains: List<Any?>? = null
    @Json(name = "created_on")
    var createdOn: String? = null
    @Json(name = "discount_amount")
    var discountAmount: Int? = null
    @Json(name = "discount_items")
    var discountItems: List<Any?>? = null
    @Json(name = "discount_rate")
    var discountRate: Int? = null
    @Json(name = "discount_reason")
    var discountReason: Any? = null
    @Json(name = "discount_value")
    var discountValue: Int? = null
    @Json(name = "distributed_discount_amount")
    var distributedDiscountAmount: Int? = null
    @Json(name = "height_text_term_compo")
    var heightTextTermCompo: Int? = null
    @Json(name = "id")
    var id: Int? = null
    @Json(name = "is_composite")
    var isComposite: Boolean? = null
    @Json(name = "is_freeform")
    var isFreeform: Boolean? = null
    @Json(name = "is_packsize")
    var isPacksize: Boolean? = null
    @Json(name = "line_amount")
    var lineAmount: Double? = null
    @Json(name = "line_promotion_type")
    var linePromotionType: Any? = null
    @Json(name = "lots_dates")
    var lotsDates: Any? = null
    @Json(name = "lots_number_code1")
    var lotsNumberCode1: Any? = null
    @Json(name = "lots_number_code2")
    var lotsNumberCode2: Any? = null
    @Json(name = "lots_number_code3")
    var lotsNumberCode3: Any? = null
    @Json(name = "lots_number_code4")
    var lotsNumberCode4: Any? = null
    @Json(name = "modified_on")
    var modifiedOn: String? = null
    @Json(name = "note")
    var note: Any? = null
    @Json(name = "pack_size_quantity")
    var packSizeQuantity: Double? = null
    @Json(name = "pack_size_root_id")
    var packSizeRootId: Any? = null
    @Json(name = "price")
    var price: Double? = null
    @Json(name = "product_id")
    var productId: Int? = null
    @Json(name = "product_name")
    var productName: String? = null
    @Json(name = "product_type")
    var productType: String? = null
    @Json(name = "quantity")
    var quantity: Double? = null
    @Json(name = "serials")
    var serials: List<Any?>? = null
    @Json(name = "sku")
    var sku: String? = null
    @Json(name = "tax_amount")
    var taxAmount: Double? = null
    @Json(name = "tax_included")
    var taxIncluded: Boolean? = null
    @Json(name = "tax_rate")
    var taxRate: Double? = null
    @Json(name = "tax_rate_override")
    var taxRateOverride: Double? = null
    @Json(name = "tax_type_id")
    var taxTypeId: Any? = null
    @Json(name = "unit")
    var unit: String? = null
    @Json(name = "variant_id")
    var variantId: Int? = null
    @Json(name = "variant_name")
    var variantName: String? = null
    @Json(name = "variant_options")
    var variantOptions: String? = null
    @Json(name = "warranty")
    var warranty: Any? = null

    constructor(orderLineItem: OrderLineItem): this(){
        productId = orderLineItem.productId
        variantId = orderLineItem.variantId
        price = orderLineItem.price
        quantity = orderLineItem.quantity
    }
}
