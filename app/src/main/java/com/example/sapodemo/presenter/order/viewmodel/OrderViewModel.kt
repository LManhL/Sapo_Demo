package com.example.sapodemo.presenter.order.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sapodemo.api.model.order.OrderSourceResponse
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.presenter.model.OrderSource
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.presenter.model.ProductPrototype

class OrderViewModel: ViewModel() {
    
    val itemSelectedHashMap = MutableLiveData<HashMap<Int, ProductOrder>>()
    val itemSelectedList = MutableLiveData<MutableList<ProductOrder>>()
    val productOrderList = MutableLiveData<MutableList<ProductOrder>>()
    val orderSourceList = MutableLiveData<MutableList<OrderSource>>()
    val selectedOrderSource = MutableLiveData<Pair<Int, OrderSource>>()

    fun convertOrderSourceResponseListAndAddToOrderSourceList(orderSourceResponseList: MutableList<OrderSourceResponse>){
       val tmpList = mutableListOf<OrderSource>()
        orderSourceResponseList.forEach {
            tmpList.add(OrderSource(it))
        }
        tmpList[0].isSelect = true
        selectedOrderSource.postValue(Pair(0,tmpList[0]))
        orderSourceList.postValue(tmpList)
    }

    fun updateSelectedOrderSource(orderSource: OrderSource, position: Int){
        selectedOrderSource.postValue(Pair(position,orderSource))
    }

    fun updateItemOfOrderSourceList(orderSource: OrderSource, position: Int){
        val tmpList = this.orderSourceList.value
        tmpList?.set(position,orderSource)
        this.orderSourceList.postValue(tmpList)
    }

    fun updateItemOfSelectedHashmap(productOrder: ProductOrder){
        itemSelectedHashMap.value?.set(productOrder.id!!, productOrder)
    }
    fun removeItemOfSelectedHashmap(productOrder: ProductOrder){
        itemSelectedHashMap.value?.remove(productOrder.id!!)
    }
    fun updateItemOfProductOrderList(position: Int, productOrder: ProductOrder){
        val tmpList = this.productOrderList.value
        tmpList?.set(position, productOrder)
        this.productOrderList.postValue(tmpList)
    }
    fun updateItemOfItemSelectedList(position: Int, productOrder: ProductOrder){
        val tmpList = this.itemSelectedList.value
        tmpList?.set(position,productOrder)
        this.itemSelectedList.postValue(tmpList)
    }
    fun removeItemOfItemSelectedList(productOrder: ProductOrder){
        val tmpList = this.itemSelectedList.value
        tmpList?.remove(productOrder)
        this.itemSelectedList.postValue(tmpList)
    }
    fun addItemToProductOrderList(productOrder: ProductOrder){
        val curList = this.productOrderList.value?.toMutableList()
        curList?.add(productOrder)
        this.productOrderList.postValue(curList)
    }
    fun removeLastItemOfProductOrderList(){
        val curList = this.productOrderList.value?.toMutableList()
        curList?.let {
            curList.removeLast()
        }
        this.productOrderList.postValue(curList)
    }
    fun convertVariantResponseListAndAddToProductOrderList(variantResponses: MutableList<VariantResponse>, orderLineItemListPresenter: HashMap<Int, ProductOrder>){
        var tmpList = mutableListOf<ProductOrder>()
        if(this.productOrderList.value?.isNotEmpty() == true){
            tmpList = this.productOrderList.value?.toMutableList()!!
            if(tmpList.last().id == ProductPrototype.ID_LOADING) tmpList.removeLast()
        }
        variantResponses.forEach {
            val productOrder = ProductOrder(it)
            if(orderLineItemListPresenter.contains(productOrder.id)){
                productOrder.quantity = orderLineItemListPresenter[productOrder.id]?.quantity ?: 0.0
            }
            else productOrder.quantity = 0.0
            tmpList.add(productOrder)
        }
        this.productOrderList.postValue(tmpList.toMutableList())
    }
    fun convertItemSelectedHashmapToItemSelectedList(){
        val tmpList = mutableListOf<ProductOrder>()
        itemSelectedHashMap.value?.toSortedMap()?.forEach { (_,value)->
            tmpList.add(value)
        }
        itemSelectedList.postValue(tmpList)
    }
}