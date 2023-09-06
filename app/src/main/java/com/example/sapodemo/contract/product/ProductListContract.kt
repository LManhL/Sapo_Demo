package com.example.sapodemo.contract.product

import com.example.sapodemo.api.config.API_RESULT

class ProductListContract {
    interface ProductListPresenter {
        suspend fun init(query: String,type: Int)
        suspend fun initProduct(query: String)
        suspend fun initVariant(query: String)
        suspend fun loadMoreData(query: String,type: Int)
        suspend fun loadMoreProduct(query: String)
        suspend fun loadMoreVariant(query: String)
        suspend fun swipeRefresh()
        fun addLoadingItem(type: Int)
    }
    interface ProductListView {
        fun init()
        fun loadMore()
        fun swipeRefresh()
        fun updateViewLoadMore(res: API_RESULT, response: String, enableLoadMore: Boolean)
        fun updateViewSwipeRefresh()
        fun updateViewInit(res: API_RESULT, response: String, enableLoadMore: Boolean)
    }
}