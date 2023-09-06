package com.example.sapodemo.presenter.product.viewmodel

import androidx.lifecycle.*
import com.example.sapodemo.api.model.product.ProductResponse
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.model.Product
import com.example.sapodemo.model.ProductPrototype
import com.example.sapodemo.model.Variant


class ProductListViewModel: ViewModel() {
    val products = MutableLiveData<MutableList<Product>>()
    val variants = MutableLiveData<MutableList<Variant>>()
    val totalProductList = MutableLiveData<Int>()
    val totalVariantList = MutableLiveData<Int>()

    init {
        totalProductList.value = 0
        totalVariantList.value = 0
    }
    fun addToProductList(product: Product){
        val curList = this.products.value?.toMutableList()
        curList?.add(product)
        this.products.postValue(curList)
    }
    fun addToVariantList(variant: Variant){
        val curList = this.variants.value?.toMutableList()
        curList?.add(variant)
        this.variants.postValue(curList)
    }
    fun removeLast(){
        val curList = this.products.value?.toMutableList()
        curList?.removeLast()
        this.products.postValue(curList)
    }
    fun addProductModelListFromResponse(productResponses: MutableList<ProductResponse>){
        var tmpList = mutableListOf<Product>()
        if(this.products.value?.isNotEmpty() == true){
            tmpList = this.products.value!!.toMutableList()
            if(tmpList.last().id == ProductPrototype.ID_LOADING) tmpList.removeLast()
        }
        productResponses.forEach{
            it.let {
                tmpList.add(Product(it))
            }
        }
        this.products.postValue(tmpList.toMutableList())
    }
    fun addVariantModelListFromResponse(variantResponses: MutableList<VariantResponse>){
        var tmpList = mutableListOf<Variant>()
        if(this.variants.value?.isNotEmpty() == true){
            tmpList = this.variants.value?.toMutableList()!!
            if(tmpList.last().id == ProductPrototype.ID_LOADING) tmpList.removeLast()
        }
        for(variant in variantResponses){
            variant.let {
                tmpList.add(Variant(it))
            }
        }
        this.variants.postValue(tmpList.toMutableList())
    }
}