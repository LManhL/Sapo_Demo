package com.example.sapodemo.contract.product

import com.example.sapodemo.data.network.config.API_RESULT

class ProductListContract {
    interface ProductListPresenter {
        suspend fun init(query: String,type: Int)
        suspend fun loadMoreData(query: String,type: Int)
    }
    interface ProductListView {
        fun initData()
        fun loadMore()
        fun swipeRefresh()
        fun updateViewLoadMore(res: API_RESULT, response: String, enableLoadMore: Boolean)
        fun updateViewInit(res: API_RESULT, response: String, enableLoadMore: Boolean)
    }
}