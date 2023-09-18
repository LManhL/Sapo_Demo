package com.example.sapodemo.presenter.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.data.network.model.product.ProductResponse
import com.example.sapodemo.presenter.model.Product

class ProductDetailViewModel: ViewModel() {
    var product = MutableLiveData<Product>()
    fun convertResponseToProduct(productResponse: ProductResponse){
        product.postValue(Product(productResponse))
    }
}