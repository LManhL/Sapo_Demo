package com.example.sapodemo.presenter.order.orderpresenter

import com.example.sapodemo.api.config.API_RESULT
import com.example.sapodemo.api.config.ApiQuery
import com.example.sapodemo.api.model.product.VariantResponse
import com.example.sapodemo.api.repos.VariantRepos
import com.example.sapodemo.presenter.model.MetadataModel
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.contract.order.ProductSelectionContract
import com.example.sapodemo.presenter.order.viewmodel.OrderViewModel

class ProductSelectionPresenter(private val view: ProductSelectionContract.ProductSelectionView, private val model: OrderViewModel): ProductSelectionContract.ProductSelectionPresenter {
    private var page = ApiQuery.PAGE
    private var limit = ApiQuery.LIMIT
    private var currentItemSelectedHashmap = HashMap<Int, ProductOrder>()

    init {
        model.itemSelectedHashMap.value?.let { currentItemSelectedHashmap.putAll(it) }
    }

    override suspend fun init(query: String) {
        model.productOrderList.postValue(mutableListOf())
        page = ApiQuery.PAGE
        val res = if(query.isEmpty()) VariantRepos.API.getVariants(page,limit)
        else VariantRepos.API.searchVariant(query,page,limit)
        if(res.isSuccessful){
            page += ApiQuery.PAGE
            res.body()?.let {
                val resVariantResponses = res.body()!!.variantResponses as MutableList<VariantResponse>
                val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                if(resVariantResponses.isNotEmpty()){
                    model.convertVariantResponseListAndAddToProductOrderList(resVariantResponses, currentItemSelectedHashmap)
                    view.updateViewInit(API_RESULT.SUCCESS,res.message(), enableLoadMore)
                }
                else{
                    view.updateViewInit(API_RESULT.EMPTYLIST,res.message(), enableLoadMore)
                }
            }
        }
        else view.updateViewInit(API_RESULT.ERROR,res.message(), MetadataModel.DISABLE_LOAD_MORE)
    }

    override suspend fun loadMoreData(query: String) {
        addLoadingItem()
        val res = if(query.isNotEmpty()){
            VariantRepos.API.searchVariant(query,page,limit)
        }
        else VariantRepos.API.getVariants(page,limit)
        if(res.isSuccessful){
            res.body()?.let {
                val resList = res.body()!!.variantResponses as MutableList<VariantResponse>
                val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                if(resList.isNotEmpty()){
                    model.convertVariantResponseListAndAddToProductOrderList(resList,currentItemSelectedHashmap)
                    page += ApiQuery.PAGE
                    view.updateViewLoadMore(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                }
                else {
                    model.removeLastItemOfProductOrderList()
                    view.updateViewLoadMore(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                }
            }
        }
        else{
            model.removeLastItemOfProductOrderList()
            view.updateViewLoadMore(API_RESULT.ERROR, res.message(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override fun select(productOrder: ProductOrder, position: Int) {
        val productOrderCopy = productOrder.copyOf()
        productOrderCopy.quantity += 1
        model.updateItemOfProductOrderList(position,productOrderCopy)
        currentItemSelectedHashmap[productOrder.id!!] = productOrderCopy
    }

    override fun submit() {
        model.itemSelectedHashMap.postValue(currentItemSelectedHashmap)
    }

    override fun reselect() {
        currentItemSelectedHashmap.clear()
        model.itemSelectedHashMap.value?.let { currentItemSelectedHashmap.putAll(it) }
        val tmpList = model.productOrderList.value?.toMutableList()
        tmpList?.let { list->
            for((index,productOrder) in list.withIndex()){
                if(productOrder.quantity != 0.0){
                    val productOrderCopy = productOrder.copyOf()
                    productOrderCopy.quantity = currentItemSelectedHashmap[productOrderCopy.id]?.quantity ?: 0.0
                    tmpList[index] = productOrderCopy
                }
            }
        }
        model.productOrderList.postValue(tmpList)
    }
    private fun addLoadingItem() {
        model.addItemToProductOrderList(ProductOrder.getItemLoading())
    }

}