package com.example.sapodemo.model


class Order(){
    var sourceId: Int = 0
    var status: String = ""
    var orderLineItems = mutableListOf<OrderLineItem>()

    constructor(sourceId: Int, status: String, orderLineItems: MutableList<OrderLineItem>): this(){
        this.sourceId = sourceId
        this.status = status
        this.orderLineItems.addAll(orderLineItems)
    }
}