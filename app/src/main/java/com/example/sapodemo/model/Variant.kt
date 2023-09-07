package com.example.sapodemo.model

import com.example.sapodemo.api.model.product.ImageVariantResponse
import com.example.sapodemo.api.model.product.InventoryResponse
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.model.formatter.FormatNumberUtil


open class Variant(): ProductPrototype() {

    companion object{
        const val EMPTY_STRING = ""
        const val EMPTY_NUMBER = 0
        const val PERCENT = "%"

        fun getVariantLoading(): Variant {
            val variant = Variant()
            variant.id = ID_LOADING
            return variant
        }
    }

    val images: MutableList<Image> = mutableListOf()
    val inventories: MutableList<Inventory> = mutableListOf()
    var barcode: String? = null
    var brandId: Any? = null
    var categoryId: Int? = null
    var description: String? = null
    var imageId: Int? = null
    var inputVatRate: Double? = null
    var locationId: Int? = null
    var opt1: String? = null
    var opt2: String? = null
    var opt3: String? = null
    var outputVatRate: Double? = null
    var packsize: Boolean? = null
    var packsizeRootId: Int? = null
    var productId: Int? = null
    var sellable: Boolean? = null
    var sku: String? = null
    var unit: String? = null
    var variantImportPrice: Double = 0.0
    var variantRetailPrice: Double = 0.0
    var variantWholePrice: Double = 0.0
    var weightUnit: String? = null
    var weightValue: Double? = null

    constructor(variantResponse: VariantResponse): this(){
        convertVariantResponseToModel(variantResponse)
    }

    fun convertVariantResponseToModel(variantResponse: VariantResponse){
        super.id = variantResponse.id
        super.name = variantResponse.name
        super.status = variantResponse.status
        variantResponse.images?.let { convertImageListFromResponseToModel(it.toMutableList()) }
        convertInventoryListFromResponseToModel(variantResponse.inventories.toMutableList())
        productId = variantResponse.productId
        barcode = variantResponse.barcode
        brandId = variantResponse.brandId
        categoryId = variantResponse.categoryId
        description = variantResponse.description
        imageId = variantResponse.imageId
        inputVatRate = variantResponse.inputVatRate
        locationId = variantResponse.locationId
        opt1 = variantResponse.opt1
        opt2 = variantResponse.opt2
        opt3 = variantResponse.opt3
        outputVatRate = variantResponse.outputVatRate
        packsize = variantResponse.packsize
        packsizeRootId = variantResponse.packsizeRootId
        sellable = variantResponse.sellable
        sku = variantResponse.sku
        unit = variantResponse.unit
        variantResponse.variantImportPrice?.let { variantImportPrice = it }
        variantResponse.variantRetailPrice?.let { variantRetailPrice = it }
        variantResponse.variantWholePrice?.let { variantWholePrice = it }
        weightUnit = variantResponse.unit
        weightValue = variantResponse.weightValue
    }
    fun skuToString(): String{
        return sku ?: EMPTY_STRING
    }
    fun weightToString(): String{
        return weightValueToString()+weightUnitToString()
    }
    fun getTotalAvailableToString(): String{
        return FormatNumberUtil.formatNumberCeil(calculateTotalAvailable())
    }
    fun wholePriceToString(): String{
        return FormatNumberUtil.formatNumberCeil(variantWholePrice)
    }
    fun importPriceToString(): String{
        return FormatNumberUtil.formatNumberCeil(variantImportPrice)
    }
    fun descriptionToString(): String {
        return if(description == null) EMPTY_STRING
        else description.toString()
    }

    fun inputVatRateToString(): String {
        return if(inputVatRate == null) "$EMPTY_NUMBER $PERCENT"
        else FormatNumberUtil.formatNumberCeil(inputVatRate!!) + PERCENT
    }

    fun outputVatRateToString(): String {
        return if(outputVatRate == null) "$EMPTY_NUMBER $PERCENT"
        else FormatNumberUtil.formatNumberCeil(outputVatRate!!) + PERCENT
    }
    fun nameToString(): String{
        return name ?: EMPTY_STRING
    }

    fun opt1ToString(): String {
        return opt1 ?: EMPTY_STRING
    }

    fun opt2ToString(): String {
        return opt2 ?: EMPTY_STRING
    }

    fun opt3ToString(): String {
        return opt3 ?: EMPTY_STRING
    }

    fun getTotalOnhandToString(): String{
        return FormatNumberUtil.formatNumberCeil(calculateTotalOnHand())
    }
    fun retailPriceToString(): String{
        return FormatNumberUtil.formatNumberCeil(variantRetailPrice)
    }
    fun barcodeToString(): String{
        return barcode ?: EMPTY_STRING
    }
    fun isSellable(): Boolean{
        return sellable ?: true
    }
    fun isActive(): Boolean{
        return when(status){
            "active"-> true
            else -> false
        }
    }
    fun weightValueToString(): String{
        return if(weightValue == null) EMPTY_STRING
        else FormatNumberUtil.formatNumberCeil(weightValue!!)
    }
    fun weightUnitToString(): String{
        return weightUnit ?: EMPTY_STRING
    }
    fun onHandToString(): String{
        return FormatNumberUtil.formatNumberCeil(calculateTotalOnHand())
    }
    fun availableToString(): String{
        return FormatNumberUtil.formatNumberCeil(calculateTotalAvailable())
    }
    fun getImagePath(): String? {
        return images.firstOrNull { it.id == imageId }?.fullPath
    }
    fun unitToString(): String{
        return unit ?: EMPTY_STRING
    }
    fun calculateTotalAvailable(): Double{
        return inventories.sumOf { it.available ?: 0.0 }
    }
    fun calculateTotalOnHand(): Double{
        return inventories.sumOf { it.onHand ?: 0.0 }
    }
    private fun convertImageListFromResponseToModel(imageResponseList: MutableList<ImageVariantResponse>){
        imageResponseList.forEach{
            images.add(Image(it))
        }
    }
    private fun convertInventoryListFromResponseToModel(inventoryList: MutableList<InventoryResponse>){
        inventoryList.forEach{
            inventories.add(Inventory(it))
        }
    }
}