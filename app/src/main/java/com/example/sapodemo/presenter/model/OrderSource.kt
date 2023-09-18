package com.example.sapodemo.presenter.model

import com.example.sapodemo.data.network.model.order.OrderSourceResponse

data class OrderSource (
    var id: Int? = null,
    var name: String? = "TikTok Shop",
    var isSelect: Boolean = false
    ){
    constructor(orderSourceResponse: OrderSourceResponse): this(){
        this.id = orderSourceResponse.id
        this.name = orderSourceResponse.name
        this.isSelect = false
    }
}