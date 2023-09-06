package com.example.sapodemo.model

import com.example.sapodemo.api.model.order.OrderSourceResponse

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