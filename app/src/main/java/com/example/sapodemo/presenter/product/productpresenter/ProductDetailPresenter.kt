package com.example.sapodemo.presenter.product.productpresenter

import com.example.sapodemo.api.repos.ProductRepos
import com.example.sapodemo.contract.product.ProductDetailContract
import com.example.sapodemo.presenter.product.viewmodel.ProductDetailViewModel

class ProductDetailPresenter(productDetailView: ProductDetailContract.ProductDetailView, _model: ProductDetailViewModel): ProductDetailContract.ProductDetailPresenter {
    private var model: ProductDetailViewModel = _model
    private var view: ProductDetailContract.ProductDetailView = productDetailView
    override suspend fun getProduct(id: Int) {
        val res = ProductRepos.API.getProduct(id)
        if(res.isSuccessful){
            res.body()?.let {
                val product = res.body()!!.productResponse
                model.convertResponseToProduct(product)
                view.onSuccess(res.message())
            }
        }
        else view.onFail(res.message())
    }
}