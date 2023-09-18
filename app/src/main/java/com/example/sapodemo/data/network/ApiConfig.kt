package com.example.sapodemo.data.network.config

class ApiQuery{
    companion object{
        const val PAGE = 1
        const val LIMIT = 10
        const val LIMIT_QUERY = "limit"
        const val PAGE_QUERY = "page"
        const val SEARCH_QUERY = "query"
    }
}
class EndPoint{
    companion object{
        const val PRODUCT_LIST_END_POINT = "admin/products.json"
        const val PRODUCT_DETAIL_END_POINT = "admin/products/{id}.json"
        const val PRODUCT_LIST_SEARCH_END_POINT = "admin/products/search.json"
        const val VARIANT_LIST_END_POINT = "admin/variants/search.json"
        const val VARIANT_DETAIL_END_POINT = "admin/products/{product_id}/variants/{variant_id}.json"
        const val VARIANT_LIST_SEARCH_END_POINT = "admin/variants/search.json"
        const val ORDER_END_POINT = "admin/orders.json"
        const val ORDER_SOURCE_END_POINT = "admin/order_sources.json"
        const val PRODUCT_ID_PATH = "product_id"
        const val VARIANT_ID_PATH = "variant_id"
        const val ID_PATH = "id"
    }
}
enum class API_RESULT{
    EMPTYLIST,
    SUCCESS,
    ERROR
}

