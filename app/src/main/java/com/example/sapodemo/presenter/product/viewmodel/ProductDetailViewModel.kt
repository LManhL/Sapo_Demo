package com.example.sapodemo.presenter.product.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.api.model.product.ProductResponse
import com.example.sapodemo.presenter.model.Product

class ProductDetailViewModel: ViewModel() {
    var product = MutableLiveData<Product>()
    fun convertResponseToProduct(productResponse: ProductResponse){
        product.postValue(Product(productResponse))
    }
}