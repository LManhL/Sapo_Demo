package com.example.sapodemo.presenter.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.data.network.model.order.OrderSourceResponse
import com.example.sapodemo.data.network.model.product.VariantResponse
import com.example.sapodemo.presenter.model.OrderSource
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.presenter.model.ProductPrototype

class OrderViewModel : ViewModel() {

    val itemSelectedHashMap = MutableLiveData<Map<Int, ProductOrder>>(emptyMap())
    val itemSelectedList = MutableLiveData<List<ProductOrder>>(emptyList())
    val productOrderList = MutableLiveData<List<ProductOrder>>(emptyList())
    val orderSourceList = MutableLiveData<List<OrderSource>>(emptyList())
    val selectedOrderSource = MutableLiveData(Pair(3532590, OrderSource()))

    fun convertOrderSourceResponseListAndAddToOrderSourceList(orderSourceResponseList: MutableList<OrderSourceResponse>) {
        val tmpList = orderSourceResponseList.map { OrderSource(it) }
        if(tmpList.isNotEmpty()){
            tmpList[0].isSelect = true
            selectedOrderSource.postValue(Pair(0, tmpList[0]))
            orderSourceList.postValue(tmpList)
        }
    }

    fun updateSelectedOrderSource(orderSource: OrderSource, position: Int) {
        selectedOrderSource.postValue(Pair(position, orderSource))
    }

    fun updateValueOfOrderSourceList(orderSourceList: MutableList<OrderSource>){
        this.orderSourceList.postValue(orderSourceList)
    }


    fun updateItemOfSelectedHashmap(productOrder: ProductOrder) {
        val tmpMap = itemSelectedHashMap.value?.toMutableMap() ?: mutableMapOf()
        tmpMap[productOrder.id!!] = productOrder.copyOf()
        itemSelectedHashMap.postValue(tmpMap)
    }

    fun removeItemOfSelectedHashmap(productOrder: ProductOrder) {
        val tmpMap = itemSelectedHashMap.value?.toMutableMap() ?: mutableMapOf()
        tmpMap.remove(productOrder.id!!)
        itemSelectedHashMap.postValue(tmpMap)
    }

    fun updateItemOfProductOrderList(position: Int, productOrder: ProductOrder) {
        val tmpList = this.productOrderList.value?.toMutableList()
        tmpList?.set(position, productOrder)
        this.productOrderList.postValue(tmpList)
    }

    fun updateItemOfItemSelectedList(position: Int, productOrder: ProductOrder) {
        val tmpList = this.itemSelectedList.value?.toMutableList()
        tmpList?.set(position, productOrder)
        this.itemSelectedList.postValue(tmpList)
    }

    fun removeItemOfItemSelectedList(productOrder: ProductOrder) {
        val tmpList = this.itemSelectedList.value?.toMutableList()
        tmpList?.remove(productOrder)
        this.itemSelectedList.postValue(tmpList)
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

    fun convertItemSelectedHashmapToItemSelectedList() {
        itemSelectedList.postValue(itemSelectedHashMap.value?.toSortedMap()?.values?.toList() ?: emptyList())
    }
}