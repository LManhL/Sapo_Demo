package com.example.sapodemo.presenter.model

import com.example.sapodemo.data.network.model.product.VariantResponse
import com.example.sapodemo.presenter.util.FormatNumberUtil

class ProductOrder() : Variant() {

    companion object{

        const val MAX_QUANTITY = 999999.999
        fun getItemLoading(): ProductOrder {
            val productOrder = ProductOrder()
            productOrder.id = ID_LOADING
            return productOrder
        }
    }

    var quantity: Double = 0.0

    constructor(variantResponse: VariantResponse): this(){
        super.convertVariantResponseToModel(variantResponse)
    }
    fun priceToString(): String{
        return calculatePrice().toString()
    }
    fun calculatePrice(): Double{
        return variantRetailPrice * quantity
    }
    fun quantityToString(): String{
        return FormatNumberUtil.formatNumberCeil(quantity)
    }
    fun truncateQuantityToString(): String{
        return FormatNumberUtil.formatNumberCeilAndTruncate(quantity)
    }
    fun copyOf(): ProductOrder {
        val productOrderCopy = ProductOrder()
        productOrderCopy.quantity = quantity
        productOrderCopy.id = id
        productOrderCopy.name = name
        productOrderCopy.status = status
        productOrderCopy.images.addAll(images)
        productOrderCopy.inventories.addAll(inventories)
        productOrderCopy.productId = productId
        productOrderCopy.barcode = barcode
        productOrderCopy.brandId = brandId
        productOrderCopy.categoryId = categoryId
        productOrderCopy.description = description
        productOrderCopy.imageId = imageId
        productOrderCopy.inputVatRate = inputVatRate
        productOrderCopy.locationId = locationId
        productOrderCopy.opt1 = opt1
        productOrderCopy.opt2 = opt2
        productOrderCopy.opt3 = opt3
        productOrderCopy.outputVatRate = outputVatRate
        productOrderCopy.packsize = packsize
        productOrderCopy.packsizeRootId = packsizeRootId
        productOrderCopy.sellable = sellable
        productOrderCopy.sku = sku
        productOrderCopy.unit = unit
        productOrderCopy.variantImportPrice = variantImportPrice
        productOrderCopy.variantRetailPrice = variantRetailPrice
        productOrderCopy.variantWholePrice = variantWholePrice
        productOrderCopy.weightUnit = unit
        productOrderCopy.weightValue = weightValue
        return productOrderCopy
    }
}