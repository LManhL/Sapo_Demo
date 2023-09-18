package com.example.sapodemo.presenter.product

import androidx.compose.ui.window.Dialog
import com.example.sapodemo.data.network.config.API_RESULT
import com.example.sapodemo.data.network.config.ApiQuery
import com.example.sapodemo.contract.product.ProductListContract
import com.example.sapodemo.data.manager.AppDataManager
import com.example.sapodemo.data.network.model.product.JsonProductsResponse
import com.example.sapodemo.data.network.model.product.JsonVariantsResponse
import com.example.sapodemo.presenter.model.MetadataModel
import com.example.sapodemo.presenter.model.Product
import com.example.sapodemo.presenter.model.ProductPrototype
import com.example.sapodemo.presenter.model.Variant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductListPresenter(
    private val view: ProductListContract.ProductListView,
    private val viewModel: ProductListViewModel,
    private val appDataManager: AppDataManager
) : ProductListContract.ProductListPresenter {
    private var pageOfProduct = ApiQuery.PAGE
    private var limit = ApiQuery.LIMIT

    override suspend fun init(query: String, type: Int) {
        try {
            viewModel.products.postValue(mutableListOf())
            pageOfProduct = ApiQuery.PAGE
            val res: Response<*> = callApi(query, type)
            if (res.isSuccessful) {
                pageOfProduct += ApiQuery.PAGE
                res.body()?.let { responseBody ->

                    val resList: List<Any>
                    val enableLoadMore: Boolean

                    if (type == ProductPrototype.PRODUCT_TYPE) {
                        val productResponse = responseBody as JsonProductsResponse
                        resList = productResponse.productResponses
                        enableLoadMore =
                            MetadataModel(productResponse.metadataResponse).enableLoadMore()
                        viewModel.total.postValue(responseBody.metadataResponse.total)
                    } else {
                        val variantResponse = responseBody as JsonVariantsResponse
                        resList = variantResponse.variantResponses
                        enableLoadMore =
                            MetadataModel(variantResponse.metadataResponse).enableLoadMore()
                        viewModel.total.postValue(responseBody.metadataResponse.total)
                    }
                    if (resList.isNotEmpty()) {
                        viewModel.addProductModelListFromResponse(resList as MutableList<Any>, type)
                        view.updateViewInit(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                    } else {
                        view.updateViewInit(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                    }
                }
            }
            else{
                view.updateViewInit(API_RESULT.ERROR, res.message().toString(), MetadataModel.DISABLE_LOAD_MORE)
            }
        }
        catch (e: Exception){
            view.updateViewInit(API_RESULT.ERROR, e.message.toString(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override suspend fun loadMoreData(query: String, type: Int) {
        try {
            addLoadingItem(type)
            val res: Response<*> = callApi(query, type)
            if (res.isSuccessful) {
                res.body()?.let { responseBody ->

                    val resList: List<Any>
                    val enableLoadMore: Boolean

                    if (type == ProductPrototype.PRODUCT_TYPE) {
                        val productResponse = responseBody as JsonProductsResponse
                        resList = productResponse.productResponses
                        enableLoadMore = MetadataModel(productResponse.metadataResponse).enableLoadMore()
                    } else {
                        val variantResponse = responseBody as JsonVariantsResponse
                        resList = variantResponse.variantResponses
                        enableLoadMore =
                            MetadataModel(variantResponse.metadataResponse).enableLoadMore()
                    }

                    if (resList.isNotEmpty()) {
                        viewModel.addProductModelListFromResponse(resList as MutableList<Any>, type)
                        view.updateViewLoadMore(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                        pageOfProduct += ApiQuery.PAGE
                    } else {
                        viewModel.removeLast()
                        view.updateViewLoadMore(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                    }
                }
            }
            else {
                viewModel.removeLast()
                view.updateViewLoadMore(API_RESULT.ERROR, res.message().toString(), MetadataModel.DISABLE_LOAD_MORE)
            }
        }
        catch (e: Exception){
            viewModel.removeLast()
            view.updateViewLoadMore(API_RESULT.ERROR, e.message.toString(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    private suspend fun callApi(query: String, type: Int): Response<*> {
        return if (type == ProductPrototype.PRODUCT_TYPE) {
                if (query.isEmpty()) appDataManager.getProducts(pageOfProduct, limit)
                else appDataManager.searchProduct(query,pageOfProduct,limit)
            } else {
                if (query.isEmpty()) appDataManager.getVariants(pageOfProduct, limit)
                else appDataManager.searchVariant(query,pageOfProduct,limit)
            }
    }

    private fun addLoadingItem(type: Int) {
        if (type == ProductPrototype.PRODUCT_TYPE) viewModel.addToProductList(Product.getLoadingProduct())
        else viewModel.addToProductList(Variant.getVariantLoading())
    }
}