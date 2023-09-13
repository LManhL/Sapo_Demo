package com.example.sapodemo.presenter.order.orderpresenter

import android.util.Log
import com.example.sapodemo.api.model.order.OrderPost
import com.example.sapodemo.presenter.model.OrderLineItem
import com.example.sapodemo.presenter.model.Order
import com.example.sapodemo.api.repos.OrderRepos
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.contract.order.OrderContract
import com.example.sapodemo.presenter.model.OrderSource
import com.example.sapodemo.presenter.order.viewmodel.OrderViewModel
import com.example.sapodemo.util.FormatNumberUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderPresenter(val view: OrderContract.OrderView, val model: OrderViewModel) :
    OrderContract.OrderPresenter {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val res = OrderRepos.API.getOrderSources()
            if (res.isSuccessful) {
                res.body()?.let {
                    val resList = it.orderSourceResponses?.toMutableList()!!
                    model.convertOrderSourceResponseListAndAddToOrderSourceList(resList)
                }
            }
        }
    }

    override fun handleMinus(productOrder: ProductOrder, position: Int) {
        if (productOrder.quantity > 1) {
            val productOrderCopy = productOrder.copyOf()
            productOrderCopy.quantity -= 1
            model.updateItemOfItemSelectedList(position, productOrderCopy)
            model.updateItemOfSelectedHashmap(productOrderCopy)
        } else {
            view.onClickCancelItem(productOrder, position)
        }
    }

    override fun handleAdd(productOrder: ProductOrder, position: Int) {
        val productOrderCopy = productOrder.copyOf()
        productOrderCopy.quantity += 1
        model.updateItemOfItemSelectedList(position, productOrderCopy)
        model.updateItemOfSelectedHashmap(productOrderCopy)
    }

    override fun handleCancel(productOrder: ProductOrder, position: Int) {
        model.removeItemOfItemSelectedList(productOrder)
        model.removeItemOfSelectedHashmap(productOrder)
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
            model.updateItemOfItemSelectedList(position, productOrderCopy)
            model.updateItemOfSelectedHashmap(productOrderCopy)
        }
    }

    override fun handleSelectOrderSource(newOrderSource: OrderSource, newPosition: Int) {
        val oldPosition = model.selectedOrderSource.value!!.first
        val oldOrderSource = model.selectedOrderSource.value!!.second.copy(isSelect = false)
        val newOrderSourceCopy = newOrderSource.copy(isSelect = true)
        model.updateItemOfOrderSourceList(oldOrderSource, oldPosition)
        model.updateItemOfOrderSourceList(newOrderSourceCopy, newPosition)
        model.updateSelectedOrderSource(newOrderSourceCopy, newPosition)
    }

    override suspend fun createOrder() {
        val sourceId = model.selectedOrderSource.value?.second?.id ?: 3532590
        val status = "draft"
        val orderLineItemList =
            model.itemSelectedList.value?.map { OrderLineItem(it) }?.toMutableList()
                ?: mutableListOf()

        if (orderLineItemList.isEmpty()) {
            view.onFailCreateOrder("Empty List")
        } else {
            val order = Order(
                sourceId = sourceId,
                status = status,
                orderLineItems = orderLineItemList
            )
            val res = OrderRepos.API.postOrder(401343, OrderPost(order))
            if (res.isSuccessful) {
                view.onCreateOrderSuccess()
                Log.d("createorder", res.body()!!.orderPost!!.id.toString())
            } else view.onFailCreateOrder(res.message())
        }
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
        return model.itemSelectedList.value?.let { list -> list.sumOf { it.calculatePrice() } }
            ?: 0.0
    }

    private fun calculateTotalTax(): Double {
        return model.itemSelectedList.value?.let { list ->
            list.sumOf { it.calculatePrice() * (it.outputVatRate ?: 0.0) }
        }?.div(100.0) ?: 0.0
    }

    private fun calculateProvisional(): Double {
        return calculateTotalPrice() + calculateTotalTax()
    }

    private fun calculateTotalQuantity(): Double {
        return model.itemSelectedList.value?.let { list -> list.sumOf { it.quantity } } ?: 0.0
    }
}