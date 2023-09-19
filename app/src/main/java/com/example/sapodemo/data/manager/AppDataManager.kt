package com.example.sapodemo.data.manager

import android.content.Context
import com.example.sapodemo.data.network.model.order.JsonOrderSourceResponse
import com.example.sapodemo.data.network.model.order.OrderPost
import com.example.sapodemo.data.network.model.order.OrderResponse
import com.example.sapodemo.data.network.model.product.JsonProductResponse
import com.example.sapodemo.data.network.model.product.JsonProductsResponse
import com.example.sapodemo.data.network.model.product.JsonVariantResponse
import com.example.sapodemo.data.network.model.product.JsonVariantsResponse
import com.example.sapodemo.data.network.repos.OrderApi
import com.example.sapodemo.data.network.repos.ProductApi
import com.example.sapodemo.data.network.repos.VariantApi
import com.example.sapodemo.data.sharepref.AppPreferencesHelper
import retrofit2.Response

class AppDataManager(context: Context): DataManager {

    private val appPreferencesHelper = AppPreferencesHelper(context)

    override fun getSelectionType(): Boolean {
        return appPreferencesHelper.getSelectionType()
    }

    override fun setSelectionType(type: Boolean) {
        appPreferencesHelper.setSelectionType(type)
    }

    override suspend fun postOrder(
        locationId: Int,
        requestBody: OrderPost
    ): Response<OrderResponse> {
        return OrderApi.API.postOrder(locationId,requestBody)
    }

    override suspend fun getOrderSources(): Response<JsonOrderSourceResponse> {
        return OrderApi.API.getOrderSources()
    }

    override suspend fun getProducts(page: Int, limit: Int): Response<JsonProductsResponse> {
        return ProductApi.API.getProducts(page,limit)
    }

    override suspend fun getProduct(id: Int): Response<JsonProductResponse> {
        return ProductApi.API.getProduct(id)
    }

    override suspend fun searchProduct(
        query: String,
        page: Int,
        limit: Int
    ): Response<JsonProductsResponse> {
        return ProductApi.API.searchProduct(query,page,limit)
    }

    override suspend fun getVariants(page: Int, limit: Int): Response<JsonVariantsResponse> {
        return VariantApi.API.getVariants(page,limit)
    }

    override suspend fun getVariant(productId: Int, variantId: Int): Response<JsonVariantResponse> {
        return VariantApi.API.getVariant(productId,variantId)
    }

    override suspend fun searchVariant(
        query: String,
        page: Int,
        limit: Int
    ): Response<JsonVariantsResponse> {
        return VariantApi.API.searchVariant(query,page,limit)
    }
}