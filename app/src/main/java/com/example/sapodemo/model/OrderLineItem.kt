package com.example.sapodemo.model


 class OrderLineItem(){
     var productId: Int = 0
     var variantId: Int = 0
     var price: Double = 0.0
     var quantity: Double = 0.0

     constructor(productOrder: ProductOrder): this(){
         productId = productOrder.productId!!
         variantId = productOrder.id!!
         price = productOrder.variantRetailPrice
         quantity = productOrder.quantity
     }
 }