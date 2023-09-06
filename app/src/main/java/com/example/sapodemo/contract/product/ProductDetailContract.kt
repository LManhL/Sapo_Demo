package com.example.sapodemo.contract.product

class ProductDetailContract {
    interface ProductDetailPresenter {
        suspend fun getProduct(id: Int)
    }
    interface ProductDetailView {
        fun onSuccess(response: String)
        fun onFail(response: String)
        fun setUpView()
        fun init(id: Int)
    }
}