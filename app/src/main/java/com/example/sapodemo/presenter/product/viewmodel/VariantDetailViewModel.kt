package com.example.sapodemo.presenter.product.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.api.model.product.ProductResponse
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.model.Product
import com.example.sapodemo.model.Variant

class VariantDetailViewModel: ViewModel() {
    var variant = MutableLiveData<Variant>()
    var unitList = MutableLiveData<MutableList<Variant>>()

    fun convertResponseToVariant(variantResponse: VariantResponse){
        variant.postValue(Variant(variantResponse))
    }
    fun getUnitListFromResponse(productResponse: ProductResponse, idVariant: Int){
        val product = Product(productResponse)
        val tmpList = product.getUnitListOfVariantById(idVariant)
        unitList.postValue(tmpList)
    }
}