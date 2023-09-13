package com.example.sapodemo.contract.order

import com.example.sapodemo.presenter.model.OrderSource
import com.example.sapodemo.presenter.model.ProductOrder

class OrderContract {
    interface OrderPresenter{
        fun handleMinus(productOrder: ProductOrder, position: Int)
        fun handleAdd(productOrder: ProductOrder, position: Int)
        fun handleCancel(productOrder: ProductOrder, position: Int)
        fun handleSubmitQuantity(productOrder: ProductOrder, position: Int, numberInStringType: String)
        fun handleSelectOrderSource(newOrderSource: OrderSource, newPosition: Int)
        suspend fun createOrder()
    }
    interface OrderView{
        fun onClickMinusItem(productOrder: ProductOrder, position: Int)
        fun onClickAddItem(productOrder: ProductOrder, position: Int)
        fun onClickCancelItem(productOrder: ProductOrder, position: Int)
        fun onClickModifyQuantityItem(productOrder: ProductOrder, position: Int)
        fun createOrder()
        fun onCreateOrderSuccess()
        fun onFailCreateOrder(message: String)
        fun onSelectOrderSource(orderSource: OrderSource, position: Int)
    }
}