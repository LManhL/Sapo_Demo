package com.example.sapodemo.presenter.product.viewmodel

import androidx.lifecycle.*
import com.example.sapodemo.api.model.product.ProductResponse
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.model.Product
import com.example.sapodemo.model.ProductPrototype
import com.example.sapodemo.model.Variant


class ProductListViewModel: ViewModel() {
    val products = MutableLiveData<MutableList<ProductPrototype>>()
    val total = MutableLiveData<Int>()

    init {
        total.value = 0
    }
    fun addToProductList(productPrototype: ProductPrototype){
        val curList = this.products.value?.toMutableList()
        curList?.add(productPrototype)
        this.products.postValue(curList)
    }
    fun removeLast(){
        val curList = this.products.value?.toMutableList()
        curList?.removeLast()
        this.products.postValue(curList)
    }
    fun addProductModelListFromResponse(productResponses: MutableList<Any>, type: Int){
        var tmpList = mutableListOf<ProductPrototype>()
        if(this.products.value?.isNotEmpty() == true){
            tmpList = this.products.value!!.toMutableList()
            if(tmpList.last().id == ProductPrototype.ID_LOADING) tmpList.removeLast()
        }
        productResponses.forEach{
            if(type == ProductPrototype.PRODUCT_TYPE) tmpList.add(Product(it as ProductResponse))
            else tmpList.add(Variant(it as VariantResponse))
        }
        this.products.postValue(tmpList.toMutableList())
    }
}