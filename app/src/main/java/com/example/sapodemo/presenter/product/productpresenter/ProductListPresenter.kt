package com.example.sapodemo.presenter.product.productpresenter

import com.example.sapodemo.presenter.product.viewmodel.ProductListViewModel
import com.example.sapodemo.api.config.API_RESULT
import com.example.sapodemo.api.config.ApiQuery
import com.example.sapodemo.api.model.product.JsonProductsResponse
import com.example.sapodemo.api.repos.ProductRepos
import com.example.sapodemo.contract.product.ProductListContract
import com.example.sapodemo.api.model.product.ProductResponse
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.api.repos.VariantRepos
import com.example.sapodemo.model.MetadataModel
import com.example.sapodemo.model.Product
import com.example.sapodemo.model.ProductPrototype
import com.example.sapodemo.model.Variant
import retrofit2.Response

class ProductListPresenter(productListView: ProductListContract.ProductListView, _model: ProductListViewModel): ProductListContract.ProductListPresenter {
    private var model: ProductListViewModel = _model
    private var view: ProductListContract.ProductListView = productListView
    private var pageOfProduct = ApiQuery.PAGE
    private var pageOfVariant = ApiQuery.PAGE
    private var limit = ApiQuery.LIMIT

    override suspend fun init(query: String,type: Int) {
        if(type == ProductPrototype.PRODUCT_TYPE) initProduct(query)
        else initVariant(query)
    }

    override suspend fun initProduct(query: String) {
        model.products.postValue(mutableListOf())
        pageOfProduct = ApiQuery.PAGE
        val res = if(query.isEmpty()) ProductRepos.API.getProducts(pageOfProduct,limit)
        else ProductRepos.API.searchProduct(query,pageOfProduct,limit)
        if(res.isSuccessful){
            pageOfProduct += ApiQuery.PAGE
            res.body()?.let {
                val resList = it.productResponses as MutableList<ProductResponse>
                val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                model.totalProductList.postValue(it.metadataResponse.total)
                if(resList.isNotEmpty()){
                    model.addProductModelListFromResponse(productResponses = resList)
                    view.updateViewInit(API_RESULT.SUCCESS, res.message(),enableLoadMore)
                }
                else{
                    view.updateViewInit(API_RESULT.EMPTYLIST, res.message(),enableLoadMore)
                }
            }
        }
        else  view.updateViewInit(API_RESULT.ERROR, res.message(),MetadataModel.DISABLE_LOAD_MORE)
    }

    override suspend fun initVariant(query: String) {
        model.variants.postValue(mutableListOf())
        pageOfVariant = ApiQuery.PAGE
        val res = if(query.isEmpty()) VariantRepos.API.getVariants(pageOfVariant,limit)
        else VariantRepos.API.searchVariant(query,pageOfVariant,limit)
        if(res.isSuccessful){
            pageOfVariant += ApiQuery.PAGE
            res.body()?.let {
                val resVariantResponses = res.body()!!.variantResponses as MutableList<VariantResponse>
                val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                model.totalVariantList.postValue(res.body()!!.metadataResponse.total)
                if(resVariantResponses.isNotEmpty()){
                    model.addVariantModelListFromResponse(resVariantResponses)
                    return view.updateViewInit(API_RESULT.SUCCESS, res.message(),enableLoadMore)
                }
                else{
                    return view.updateViewInit(API_RESULT.EMPTYLIST, res.message(),enableLoadMore)
                }
            }
        }
        else view.updateViewInit(API_RESULT.ERROR, res.message(),MetadataModel.DISABLE_LOAD_MORE)
    }

    override suspend fun loadMoreData(query: String,type: Int) {
        addLoadingItem(type)
        if(type == ProductPrototype.PRODUCT_TYPE) loadMoreProduct(query)
        else loadMoreVariant(query)
    }

    override suspend fun loadMoreProduct(query: String) {
        val res: Response<JsonProductsResponse>
                = if(query.isNotEmpty()){
            ProductRepos.API.searchProduct(query,pageOfProduct,limit)
        }
        else ProductRepos.API.getProducts(pageOfProduct,limit)
        if(res.isSuccessful){
            res.body()?.let {
                val resList = res.body()!!.productResponses as MutableList<ProductResponse>
                val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                if(resList.isNotEmpty()){
                    model.addProductModelListFromResponse(resList)
                    view.updateViewLoadMore(API_RESULT.SUCCESS, res.message(),enableLoadMore)
                    pageOfProduct += ApiQuery.PAGE
                }
                else {
                    model.removeLast()
                    view.updateViewLoadMore(API_RESULT.EMPTYLIST, res.message(),enableLoadMore)
                }
            }
        }
        else{
            model.removeLast()
            view.updateViewLoadMore(API_RESULT.ERROR, res.message(),MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override suspend fun loadMoreVariant(query: String) {
        val res = if(query.isNotEmpty()){
            VariantRepos.API.searchVariant(query,pageOfVariant,limit)
        }
        else VariantRepos.API.getVariants(pageOfVariant,limit)
        if(res.isSuccessful){
            res.body()?.let {
                val resList = res.body()!!.variantResponses as MutableList<VariantResponse>
                val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                if(resList.isNotEmpty()){
                    model.addVariantModelListFromResponse(resList)
                    pageOfVariant += ApiQuery.PAGE
                    view.updateViewLoadMore(API_RESULT.SUCCESS, res.message(),enableLoadMore)
                }
                else {
                    model.removeLast()
                    view.updateViewLoadMore(API_RESULT.EMPTYLIST, res.message(),enableLoadMore)
                }
            }
        }
        else{
            model.removeLast()
            view.updateViewLoadMore(API_RESULT.ERROR, res.message(),MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override fun addLoadingItem(type: Int) {
        if(type == ProductPrototype.PRODUCT_TYPE) model.addToProductList(Product.getLoadingProduct())
        else model.addToVariantList(Variant.getVariantLoading())
    }

    override suspend fun swipeRefresh() {
        view.init()
        view.updateViewSwipeRefresh()
    }

}