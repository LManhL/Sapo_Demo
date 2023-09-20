package com.example.sapodemo.presenter.order

import com.example.sapodemo.data.network.model.order.OrderPost
import com.example.sapodemo.presenter.model.OrderLineItem
import com.example.sapodemo.presenter.model.Order
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.contract.order.OrderContract
import com.example.sapodemo.data.manager.AppDataManager
import com.example.sapodemo.presenter.model.OrderSource
import com.example.sapodemo.presenter.util.FormatNumberUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderPresenter(
    private val view: OrderContract.OrderView,
    private val viewModel: OrderViewModel,
    private val appDataManager: AppDataManager
) : OrderContract.OrderPresenter {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val res = appDataManager.getOrderSources()
            if (res.isSuccessful) {
                res.body()?.let {
                    val resList = it.orderSourceResponses?.toMutableList()!!
                    viewModel.convertOrderSourceResponseListAndAddToOrderSourceList(resList)
                }
            }
        }
    }

    override fun handleMinus(productOrder: ProductOrder, position: Int) {
        if (productOrder.quantity > 1) {
            val productOrderCopy = productOrder.copyOf()
            productOrderCopy.quantity -= 1
            viewModel.updateItemOfItemSelectedList(position, productOrderCopy)
            viewModel.updateItemOfSelectedHashmap(productOrderCopy)
        } else {
            view.onClickCancelItem(productOrder, position)
        }
    }

    override fun handleAdd(productOrder: ProductOrder, position: Int) {
        val productOrderCopy = productOrder.copyOf()
        if(productOrderCopy.quantity <ProductOrder.MAX_QUANTITY) productOrderCopy.quantity += 1
        viewModel.updateItemOfItemSelectedList(position, productOrderCopy)
        viewModel.updateItemOfSelectedHashmap(productOrderCopy)
    }

    override fun handleCancel(productOrder: ProductOrder, position: Int) {
        viewModel.removeItemOfItemSelectedList(productOrder)
        viewModel.removeItemOfSelectedHashmap(productOrder)
    }

    override fun handleSubmitQuantity(
        productOrder: ProductOrder,
        position: Int,
        numberInStringType: String
    ) {
        val productOrderCopy = productOrder.copyOf()
        val newQuantity =
            numberInStringType.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 1.0
        if (newQuantity == 0.0) {
            view.onClickCancelItem(productOrder, position)
        } else {
            productOrderCopy.quantity = newQuantity
            viewModel.updateItemOfItemSelectedList(position, productOrderCopy)
            viewModel.updateItemOfSelectedHashmap(productOrderCopy)
        }
    }

    override fun handleSelectOrderSource(newOrderSource: OrderSource, newPosition: Int) {
        val oldPosition = viewModel.selectedOrderSource.value!!.first
        val oldOrderSourceCopy = viewModel.selectedOrderSource.value!!.second.copy(isSelect = false)
        val newOrderSourceCopy = newOrderSource.copy(isSelect = true)

        val tmpList = viewModel.orderSourceList.value?.toMutableList()
        tmpList?.apply {
            set(oldPosition,oldOrderSourceCopy)
            set(newPosition,newOrderSourceCopy)
        }
        viewModel.updateValueOfOrderSourceList(tmpList ?: mutableListOf())
        viewModel.updateSelectedOrderSource(newOrderSourceCopy, newPosition)
    }

    override suspend fun createOrder() {
        val sourceId = viewModel.selectedOrderSource.value?.second?.id ?: 3532590
        val orderLineItemList = viewModel.itemSelectedList.value?.map { OrderLineItem(it) }?.toMutableList() ?: mutableListOf()

        if (orderLineItemList.isEmpty()) {
            view.onCreateOrderFail("Empty List")
        } else {
            val order = Order(
                sourceId = sourceId,
                status = "draft",
                orderLineItems = orderLineItemList
            )
            try {
                val res = appDataManager.postOrder(401343, OrderPost(order))
                if (res.isSuccessful) {
                    view.onCreateOrderSuccess()
                }
                else view.onCreateOrderFail(res.message())
            }
            catch (e: Exception){
                view.onCreateOrderFail(e.message.toString())
            }
        }
    }

    fun setItemSelectedMap(itemSelectedMap: Map<Int,ProductOrder>){
        viewModel.itemSelectedHashMap.postValue(itemSelectedMap)
    }
    fun setItemSelectedList(itemSelectedMap: Map<Int, ProductOrder>){
        viewModel.convertItemSelectedHashmapToItemSelectedList(itemSelectedMap)
    }

    fun totalQuantityToString(): String {
        return FormatNumberUtil.formatNumberCeil(calculateTotalQuantity())
    }

    fun totalPriceToString(): String {
        return FormatNumberUtil.formatNumberCeil(calculateTotalPrice())
    }

    fun totalTaxToString(): String {
        return FormatNumberUtil.formatNumberCeil(calculateTotalTax())
    }

    fun provisionalToString(): String {
        return FormatNumberUtil.formatNumberCeil(calculateProvisional())
    }

    private fun calculateTotalPrice(): Double {
        return viewModel.itemSelectedList.value?.let { list -> list.sumOf { it.calculatePrice() } }
            ?: 0.0
    }

    private fun calculateTotalTax(): Double {
        return viewModel.itemSelectedList.value?.let { list ->
            list.sumOf { it.calculatePrice() * (it.outputVatRate ?: 0.0) }
        }?.div(100.0) ?: 0.0
    }

    private fun calculateProvisional(): Double {
        return calculateTotalPrice() + calculateTotalTax()
    }

    private fun calculateTotalQuantity(): Double {
        return viewModel.itemSelectedList.value?.let { list -> list.sumOf { it.quantity } } ?: 0.0
    }
}