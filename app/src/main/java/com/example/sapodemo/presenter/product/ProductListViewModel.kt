package com.example.sapodemo.presenter.product

import androidx.lifecycle.*
import com.example.sapodemo.data.network.model.product.ProductResponse
import com.example.sapodemo.data.network.model.product.VariantResponse
import com.example.sapodemo.presenter.model.Product
import com.example.sapodemo.presenter.model.ProductPrototype
import com.example.sapodemo.presenter.model.Variant


class ProductListViewModel: ViewModel() {
    val products = MutableLiveData<List<ProductPrototype>>()
    val total = MutableLiveData<Int>(0)

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
        val tmpList = this.products.value?.toMutableList() ?: mutableListOf()
        if(tmpList.isNotEmpty() && tmpList.last().id == ProductPrototype.ID_LOADING){
             tmpList.removeLast()
        }
        productResponses.forEach{
            if(type == ProductPrototype.PRODUCT_TYPE) tmpList.add(Product(it as ProductResponse))
            else tmpList.add(Variant(it as VariantResponse))
        }
        this.products.postValue(tmpList)
    }
}