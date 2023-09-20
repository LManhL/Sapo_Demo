package com.example.sapodemo.contract.order

import com.example.sapodemo.data.network.config.API_RESULT
import com.example.sapodemo.presenter.model.ProductOrder

class ProductSelectionContract {
    interface ProductSelectionPresenter {
        suspend fun init(query: String)
        suspend fun loadMoreData(query: String)
        fun select(productOrder: ProductOrder, position: Int)
        fun reselect()
        fun getSharePrefSelectionType(): Boolean
        fun setSharePrefSelectionType(type: Boolean)
    }

    interface ProductSelectionView {
        fun initData()
        fun loadMore()
        fun select(productOrder: ProductOrder, position: Int)
        fun reselect()
        fun submit()
        fun updateViewLoadMore(res: API_RESULT, response: String, enableLoadMore: Boolean)
        fun updateViewInit(res: API_RESULT, response: String, enableLoadMore: Boolean)
    }
}