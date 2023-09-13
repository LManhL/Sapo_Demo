package com.example.sapodemo.presenter.product.productpresenter

import com.example.sapodemo.presenter.product.viewmodel.ProductListViewModel
import com.example.sapodemo.api.config.API_RESULT
import com.example.sapodemo.api.config.ApiQuery
import com.example.sapodemo.api.model.product.*
import com.example.sapodemo.api.repos.ProductRepos
import com.example.sapodemo.contract.product.ProductListContract
import com.example.sapodemo.api.repos.VariantRepos
import com.example.sapodemo.presenter.model.MetadataModel
import com.example.sapodemo.presenter.model.Product
import com.example.sapodemo.presenter.model.ProductPrototype
import com.example.sapodemo.presenter.model.Variant
import retrofit2.Response

class ProductListPresenter(productListView: ProductListContract.ProductListView, _model: ProductListViewModel): ProductListContract.ProductListPresenter {
    private var model: ProductListViewModel = _model
    private var view: ProductListContract.ProductListView = productListView
    private var pageOfProduct = ApiQuery.PAGE
    private var limit = ApiQuery.LIMIT

    override suspend fun init(query: String,type: Int) {
        model.products.postValue(mutableListOf())
        pageOfProduct = ApiQuery.PAGE

        val res: Response<*> = if (type == ProductPrototype.PRODUCT_TYPE) {
            if (query.isEmpty()) ProductRepos.API.getProducts(pageOfProduct, limit)
            else ProductRepos.API.searchProduct(query, pageOfProduct, limit)
        } else {
            if (query.isEmpty()) VariantRepos.API.getVariants(pageOfProduct, limit)
            else VariantRepos.API.searchVariant(query, pageOfProduct, limit)
        }

        if (res.isSuccessful) {
            pageOfProduct += ApiQuery.PAGE
            res.body()?.let { responseBody ->

                val resList: List<Any>
                val enableLoadMore: Boolean

                if (type == ProductPrototype.PRODUCT_TYPE) {
                    val productResponse = responseBody as JsonProductsResponse
                    resList = productResponse.productResponses
                    enableLoadMore = MetadataModel(productResponse.metadataResponse).enableLoadMore()
                    model.total.postValue(responseBody.metadataResponse.total)
                }
                else {
                    val variantResponse = responseBody as JsonVariantsResponse
                    resList = variantResponse.variantResponses
                    enableLoadMore = MetadataModel(variantResponse.metadataResponse).enableLoadMore()
                    model.total.postValue(responseBody.metadataResponse.total)
                }

                if (resList.isNotEmpty()) {
                    model.addProductModelListFromResponse(resList as MutableList<Any>, type)
                    view.updateViewInit(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                } else {
                    view.updateViewInit(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                }

            }
        }
        else {
            view.updateViewInit(API_RESULT.ERROR, res.message(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override suspend fun loadMoreData(query: String,type: Int) {
        addLoadingItem(type)

        val res: Response<*> = if (type == ProductPrototype.PRODUCT_TYPE) {
            if (query.isEmpty()) ProductRepos.API.getProducts(pageOfProduct, limit)
            else ProductRepos.API.searchProduct(query, pageOfProduct, limit)
        } else {
            if (query.isEmpty()) VariantRepos.API.getVariants(pageOfProduct, limit)
            else VariantRepos.API.searchVariant(query, pageOfProduct, limit)
        }

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
                    enableLoadMore = MetadataModel(variantResponse.metadataResponse).enableLoadMore()
                }

                if (resList.isNotEmpty()) {
                    model.addProductModelListFromResponse(resList as MutableList<Any>, type)
                    view.updateViewLoadMore(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                    pageOfProduct += ApiQuery.PAGE
                } else {
                    model.removeLast()
                    view.updateViewLoadMore(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                }
            }
        } else {
            model.removeLast()
            view.updateViewLoadMore(API_RESULT.ERROR, res.message(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }
    override suspend fun swipeRefresh() {
        view.init()
        view.updateViewSwipeRefresh()
    }
    private fun addLoadingItem(type: Int) {
        if(type == ProductPrototype.PRODUCT_TYPE) model.addToProductList(Product.getLoadingProduct())
        else model.addToProductList(Variant.getVariantLoading())
    }
}