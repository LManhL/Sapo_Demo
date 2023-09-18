package com.example.sapodemo.presenter.model

import com.example.sapodemo.data.network.model.product.ImageProductResponse
import com.example.sapodemo.data.network.model.product.OptionResponse
import com.example.sapodemo.data.network.model.product.ProductResponse
import com.example.sapodemo.data.network.model.product.VariantResponse

open class Product() : ProductPrototype(){

    companion object{
        const val EMPTY_STRING = ""

        fun getLoadingProduct(): Product {
            val product = Product()
            product.id = ID_LOADING
            return product
        }
    }
    val variants: MutableList<Variant> = mutableListOf()
    val options: MutableList<Option> = mutableListOf()
    val images: MutableList<Image> = mutableListOf()

    constructor(productResponse: ProductResponse) : this() {
        super.id = productResponse.id
        super.name = productResponse.name
        super.status = productResponse.status
        convertVariantListFromResponseToModel(productResponse.variantResponses.toMutableList())
        productResponse.optionResponses?.let { convertOptionListFromResponseToModel(it.toMutableList()) }
        productResponse.imageProductRespons?.let { convertImageListFromResposeToModel(it.toMutableList()) }
    }

    fun nameToString(): String{
        return name?: EMPTY_STRING
    }

    fun getVersionCountToString(): String{
        return "${variants.size}"
    }
    fun getTotalAvailableToString(): String {
        return "${calculateTotalAvailable()}"
    }

    fun getUnitListOfVariantById(id: Int): MutableList<Variant>{
        val unitList: MutableList<Variant> = variants.filter { it.packsizeRootId == id }.toMutableList()
        if(unitList.isNotEmpty()){
            return unitList
        }
        return mutableListOf()
    }
    fun isMultipleVariant(): Boolean {
        val optionSize = options[0]
        if(optionSize.values.isEmpty()){
            return false
        }
        if(optionSize.values[0] == "Mặc định"){
            return false
        }
        return true
    }
    fun getImagePath(): String?{
        if(images.isNotEmpty()){
            return images[0].fullPath
        }
        return null
    }
    fun isActive(): Boolean{
        return when(status){
            "active"-> true
            else -> false
        }
    }
    private fun convertVariantListFromResponseToModel(variantListResponse: MutableList<VariantResponse>){
        variantListResponse.forEach {
            variants.add(Variant(it))
        }
    }
    private fun convertOptionListFromResponseToModel(optionListResponse: MutableList<OptionResponse>){
        optionListResponse.forEach {
            options.add(Option(it))
        }
    }
    private fun convertImageListFromResposeToModel(imageListResponse: MutableList<ImageProductResponse>){
        imageListResponse.forEach {
            images.add(Image(it))
        }
    }
    private fun calculateTotalAvailable(): Double{
        return variants.sumOf { it.calculateTotalAvailable() }
    }
}