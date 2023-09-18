package com.example.sapodemo.presenter.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.data.network.model.product.ProductResponse
import com.example.sapodemo.data.network.model.product.VariantResponse
import com.example.sapodemo.presenter.model.Product
import com.example.sapodemo.presenter.model.Variant

class VariantDetailViewModel: ViewModel() {
    var variant = MutableLiveData<Variant>()
    var unitList = MutableLiveData<List<Variant>>()

    fun convertResponseToVariant(variantResponse: VariantResponse){
        variant.postValue(Variant(variantResponse))
    }
    fun getUnitListFromResponse(productResponse: ProductResponse, idVariant: Int){
        val tmpList = Product(productResponse).getUnitListOfVariantById(idVariant)
        unitList.postValue(tmpList)
    }
}