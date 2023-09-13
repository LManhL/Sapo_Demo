package com.example.sapodemo.contract.product

class VariantDetailContract {
    interface VariantDetailPresenter{
        suspend fun getVariant(productId: Int, variantId: Int)
        suspend fun getUnitList(productId: Int, variantId: Int)
    }
    interface VariantDetailView{
        fun onSuccess(response: String)
        fun onFail(response: String)
        fun updateViewInit()
        fun init()
    }
}