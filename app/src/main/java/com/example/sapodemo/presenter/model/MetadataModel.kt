package com.example.sapodemo.presenter.model

import com.example.sapodemo.api.model.product.MetadataResponse

class MetadataModel() {

    companion object{
        const val ENABLE_LOAD_MORE = true
        const val DISABLE_LOAD_MORE = false
    }

    var limit: Int = 0
    var page: Int = 0
    var total: Int = 0

    constructor(metadata: MetadataResponse) : this(){
        limit = metadata.limit
        page = metadata.page
        total = metadata.total
    }

    fun enableLoadMore(): Boolean{
        return (limit * page) < total
    }
}