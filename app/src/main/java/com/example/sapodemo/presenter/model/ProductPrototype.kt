package com.example.sapodemo.presenter.model


open class ProductPrototype (
        open var id: Int? = null,
        open var name: String? = null,
        open var status: String? = null
){
        companion object{
                const val ID_LOADING = Int.MIN_VALUE
                const val PRODUCT_TYPE = 0
                const val VARIANT_TYPE = 1
        }
}