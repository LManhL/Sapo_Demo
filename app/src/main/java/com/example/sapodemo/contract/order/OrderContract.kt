package com.example.sapodemo.contract.order

import com.example.sapodemo.model.OrderSource
import com.example.sapodemo.model.ProductOrder

class OrderContract {
    interface OrderPresenter{
        fun handleMinus(productOrder: ProductOrder, position: Int)
        fun handleAdd(productOrder: ProductOrder, position: Int)
        fun handleCancel(productOrder: ProductOrder, position: Int)
        fun handleSubmitQuantity(productOrder: ProductOrder, position: Int, numberInStringType: String)
        fun handleSelectOrderSource(newOrderSource: OrderSource, newPosition: Int)
        fun calculateTotalQuantity(): Double
        fun calculateTotalPrice(): Double
        fun calculateTotalTax(): Double
        fun calculateProvisional(): Double
        fun totalQuantityToString(): String
        fun totalPriceToString(): String
        fun totalTaxToString(): String
        fun provisionalToString(): String
        suspend fun createOrder()
    }
    interface OrderView{
        fun onClickMinus(productOrder: ProductOrder, position: Int)
        fun onClickAdd(productOrder: ProductOrder, position: Int)
        fun onClickCancel(productOrder: ProductOrder, position: Int)
        fun createOrder()
        fun onCreateOrderSuccess()
        fun onFailCreateOrder(message: String)
        fun onSelectOrderSource(orderSource: OrderSource, position: Int)
    }
}