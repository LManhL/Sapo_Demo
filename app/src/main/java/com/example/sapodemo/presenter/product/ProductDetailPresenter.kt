package com.example.sapodemo.presenter.product

import com.example.sapodemo.contract.product.ProductDetailContract
import com.example.sapodemo.data.manager.AppDataManager

class ProductDetailPresenter(
    private val view: ProductDetailContract.ProductDetailView,
    private val viewModel: ProductDetailViewModel,
    private val appDataManager: AppDataManager
) : ProductDetailContract.ProductDetailPresenter {
    override suspend fun getProduct(id: Int) {
        try {
            val res = appDataManager.getProduct(id)
            if (res.isSuccessful) {
                res.body()?.let {
                    val product = res.body()!!.productResponse
                    viewModel.convertResponseToProduct(product)
                    view.onSuccess(res.message().toString())
                }
            }
            else view.onFail(res.message().toString())
        }
        catch (e: Exception){
            view.onFail(e.message.toString())
        }
    }
}