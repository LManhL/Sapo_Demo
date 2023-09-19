package com.example.sapodemo.presenter.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.data.network.model.product.VariantResponse
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.presenter.model.ProductPrototype

class ProductSelectionViewModel: ViewModel() {
    val itemSelectedHashMap = MutableLiveData<Map<Int, ProductOrder>>(emptyMap())
    val productOrderList = MutableLiveData<List<ProductOrder>>(emptyList())

    fun updateItemOfProductOrderList(position: Int, productOrder: ProductOrder) {
        val tmpList = this.productOrderList.value?.toMutableList()
        tmpList?.set(position, productOrder)
        this.productOrderList.postValue(tmpList)
    }
    fun addItemToProductOrderList(productOrder: ProductOrder) {
        val curList = this.productOrderList.value?.toMutableList()
        curList?.add(productOrder)
        this.productOrderList.postValue(curList)
    }

    fun removeLastItemOfProductOrderList() {
        val curList = this.productOrderList.value?.toMutableList()
        curList?.removeLast()
        this.productOrderList.postValue(curList)
    }

    fun convertVariantResponseListAndAddToProductOrderList(
        variantResponses: MutableList<VariantResponse>,
        orderLineItemListPresenter: HashMap<Int, ProductOrder>
    ) {
        val tmpList = this.productOrderList.value?.toMutableList() ?: mutableListOf()
        if (tmpList.isNotEmpty() && tmpList.last().id == ProductPrototype.ID_LOADING ) {
            tmpList.removeLast()
        }
        variantResponses.forEach {
            val productOrder = ProductOrder(it)
            productOrder.quantity = orderLineItemListPresenter[productOrder.id]?.quantity ?: 0.0
            tmpList.add(productOrder)
        }
        this.productOrderList.postValue(tmpList)
    }

}