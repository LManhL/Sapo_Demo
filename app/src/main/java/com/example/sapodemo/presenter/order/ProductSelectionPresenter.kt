package com.example.sapodemo.presenter.order

import com.example.sapodemo.data.network.config.API_RESULT
import com.example.sapodemo.data.network.config.ApiQuery
import com.example.sapodemo.data.network.model.product.JsonVariantsResponse
import com.example.sapodemo.data.network.model.product.VariantResponse
import com.example.sapodemo.presenter.model.MetadataModel
import com.example.sapodemo.presenter.model.ProductOrder
import com.example.sapodemo.contract.order.ProductSelectionContract
import com.example.sapodemo.data.manager.AppDataManager
import retrofit2.Response

class ProductSelectionPresenter(
    private val view: ProductSelectionContract.ProductSelectionView,
    private val productSelectionViewModel: ProductSelectionViewModel,
    private val appDataManager: AppDataManager,
    _itemSelectedMap: Map<Int,ProductOrder>
) : ProductSelectionContract.ProductSelectionPresenter {
    private val currentItemSelectedHashmap = HashMap<Int, ProductOrder>()
    private var page = ApiQuery.PAGE
    private var limit = ApiQuery.LIMIT

    init {
        productSelectionViewModel.itemSelectedHashMap.postValue(_itemSelectedMap)
        currentItemSelectedHashmap.putAll(_itemSelectedMap)
    }

    override suspend fun init(query: String) {
        try {
            productSelectionViewModel.productOrderList.postValue(mutableListOf())
            page = ApiQuery.PAGE
            val res = callApi(query)
            if (res.isSuccessful) {
                page += ApiQuery.PAGE
                res.body()?.let {
                    val resVariantResponses =
                        res.body()!!.variantResponses as MutableList<VariantResponse>
                    val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                    if (resVariantResponses.isNotEmpty()) {
                        productSelectionViewModel.convertVariantResponseListAndAddToProductOrderList(
                            resVariantResponses,
                            currentItemSelectedHashmap
                        )
                        view.updateViewInit(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                    } else {
                        view.updateViewInit(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                    }
                }
            }
            else view.updateViewInit(API_RESULT.ERROR, res.message().toString(), MetadataModel.DISABLE_LOAD_MORE)
        }
        catch (e: Exception){
            view.updateViewInit(API_RESULT.ERROR, e.message.toString(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override suspend fun loadMoreData(query: String) {
        try {
            addLoadingItem()
            val res = callApi(query)
            if (res.isSuccessful) {
                res.body()?.let {
                    val resList = res.body()!!.variantResponses as MutableList<VariantResponse>
                    val enableLoadMore = MetadataModel(res.body()!!.metadataResponse).enableLoadMore()

                    if (resList.isNotEmpty()) {
                        productSelectionViewModel.convertVariantResponseListAndAddToProductOrderList(resList, currentItemSelectedHashmap
                        )
                        page += ApiQuery.PAGE
                        view.updateViewLoadMore(API_RESULT.SUCCESS, res.message(), enableLoadMore)
                    } else {
                        productSelectionViewModel.removeLastItemOfProductOrderList()
                        view.updateViewLoadMore(API_RESULT.EMPTYLIST, res.message(), enableLoadMore)
                    }
                }
            }
            else {
                productSelectionViewModel.removeLastItemOfProductOrderList()
                view.updateViewLoadMore(API_RESULT.ERROR, res.message().toString(), MetadataModel.DISABLE_LOAD_MORE)
            }
        }
        catch (e: Exception){
            productSelectionViewModel.removeLastItemOfProductOrderList()
            view.updateViewLoadMore(API_RESULT.ERROR, e.message.toString(), MetadataModel.DISABLE_LOAD_MORE)
        }
    }

    override fun select(productOrder: ProductOrder, position: Int) {
        val productOrderCopy = productOrder.copyOf()
        if(productOrderCopy.quantity < ProductOrder.MAX_QUANTITY) productOrderCopy.quantity += 1
        productSelectionViewModel.updateItemOfProductOrderList(position, productOrderCopy)
        currentItemSelectedHashmap[productOrder.id!!] = productOrderCopy
    }

    override fun reselect() {
        currentItemSelectedHashmap.clear()
        productSelectionViewModel.itemSelectedHashMap.value?.let { currentItemSelectedHashmap.putAll(it) }
        val tmpList = productSelectionViewModel.productOrderList.value?.toMutableList()
        tmpList?.let { list ->
            for ((index, productOrder) in list.withIndex()) {
                if (productOrder.quantity != 0.0) {
                    val productOrderCopy = productOrder.copyOf()
                    productOrderCopy.quantity = currentItemSelectedHashmap[productOrderCopy.id]?.quantity ?: 0.0
                    list[index] = productOrderCopy
                }
            }
        }
        productSelectionViewModel.productOrderList.postValue(tmpList)
    }

    override fun getSharePrefSelectionType(): Boolean {
        return appDataManager.getSelectionType()
    }

    override fun setSharePrefSelectionType(type: Boolean) {
        return appDataManager.setSelectionType(type)
    }
    fun getItemSelectedHashMap(): HashMap<Int,ProductOrder>{
        return currentItemSelectedHashmap
    }
    private suspend fun callApi(query: String): Response<JsonVariantsResponse>{
        return if (query.isEmpty()) appDataManager.getVariants(page,limit)
        else appDataManager.searchVariant(query,page,limit)
    }
    private fun addLoadingItem() {
        productSelectionViewModel.addItemToProductOrderList(ProductOrder.getItemLoading())
    }

}