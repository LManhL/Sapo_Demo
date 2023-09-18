package com.example.sapodemo.presenter.product

import android.util.Log
import com.example.sapodemo.contract.product.VariantDetailContract
import com.example.sapodemo.data.manager.AppDataManager

class VariantDetailPresenter(
    private val view: VariantDetailContract.VariantDetailView,
    private val viewModel: VariantDetailViewModel,
    private val appDataManager: AppDataManager
) : VariantDetailContract.VariantDetailPresenter {

    override suspend fun getVariant(productId: Int, variantId: Int) {
        try {
            val res = appDataManager.getVariant(productId,variantId)
            if (res.isSuccessful) {
                res.body()?.let {
                    val variant = res.body()!!.variantResponse
                    viewModel.convertResponseToVariant(variant)
                    view.onSuccess(res.message())
                }

            }
            else view.onFail(res.message().toString())
        }
        catch (e: Exception){
            view.onFail(e.message.toString())
        }
    }

    override suspend fun getUnitList(productId: Int, variantId: Int) {
        try {
            val resProduct = appDataManager.getProduct(productId)
            if (resProduct.isSuccessful) {
                resProduct.body()?.let {
                    val productResponse = resProduct.body()!!.productResponse
                    viewModel.getUnitListFromResponse(productResponse, variantId)
                }
            }
        }
        catch (e: Exception){
            Log.d("Error: ","{${e.message}}")
        }
    }
}