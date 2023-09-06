package com.example.sapodemo.presenter.product.productpresenter

import com.example.sapodemo.api.repos.ProductRepos
import com.example.sapodemo.api.repos.VariantRepos
import com.example.sapodemo.contract.product.VariantDetailContract
import com.example.sapodemo.presenter.product.viewmodel.VariantDetailViewModel

class VariantDetailPresenter(_view: VariantDetailContract.VariantDetailView, _model: VariantDetailViewModel): VariantDetailContract.VariantDetailPresenter {
    private var view : VariantDetailContract.VariantDetailView = _view
    private var model : VariantDetailViewModel = _model
    override suspend fun getVariant(productId: Int, variantId: Int) {
        val res = VariantRepos.API.getVariant(productId,variantId)
        if(res.isSuccessful){
            res.body()?.let {
                val variant = res.body()!!.variantResponse
                model.convertResponseToVariant(variant)
                view.onSuccess(res.message())
            }
        }
        else view.onFail(res.message())
    }

    override suspend fun getUnitList(productId: Int, variantId: Int) {
        val resProduct = ProductRepos.API.getProduct(productId)
        if(resProduct.isSuccessful){
            resProduct.body()?.let {
                val productResponse = resProduct.body()!!.productResponse
                model.getUnitListFromResponse(productResponse, variantId)
            }
        }
    }
}