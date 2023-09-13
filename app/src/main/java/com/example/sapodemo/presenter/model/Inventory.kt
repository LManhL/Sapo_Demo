package com.example.sapodemo.presenter.model

import com.example.sapodemo.api.model.product.InventoryResponse


class Inventory() {
    var locationId: Int? = null
    var onHand: Double? = null
    var variantId: Int? = null
    var available: Double? = null

    constructor(inventoryResponse: InventoryResponse): this(){
        locationId = inventoryResponse.locationId
        onHand = inventoryResponse.onHand
        variantId = inventoryResponse.variantId
        available = inventoryResponse.available
    }
}