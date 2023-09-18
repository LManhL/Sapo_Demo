package com.example.sapodemo.data.manager

import android.content.Context
import android.util.Log
import com.example.sapodemo.data.network.model.order.JsonOrderSourceResponse
import com.example.sapodemo.data.network.model.order.OrderPost
import com.example.sapodemo.data.network.model.order.OrderResponse
import com.example.sapodemo.data.network.model.product.JsonProductResponse
import com.example.sapodemo.data.network.model.product.JsonProductsResponse
import com.example.sapodemo.data.network.model.product.JsonVariantResponse
import com.example.sapodemo.data.network.model.product.JsonVariantsResponse
import com.example.sapodemo.data.network.repos.OrderRepos
import com.example.sapodemo.data.network.repos.ProductRepos
import com.example.sapodemo.data.network.repos.VariantRepos
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
        return OrderRepos.API.postOrder(locationId,requestBody)
    }

    override suspend fun getOrderSources(): Response<JsonOrderSourceResponse> {
        return OrderRepos.API.getOrderSources()
    }

    override suspend fun getProducts(page: Int, limit: Int): Response<JsonProductsResponse> {
        return ProductRepos.API.getProducts(page,limit)
    }

    override suspend fun getProduct(id: Int): Response<JsonProductResponse> {
        return ProductRepos.API.getProduct(id)
    }

    override suspend fun searchProduct(
        query: String,
        page: Int,
        limit: Int
    ): Response<JsonProductsResponse> {
        return ProductRepos.API.searchProduct(query,page,limit)
    }

    override suspend fun getVariants(page: Int, limit: Int): Response<JsonVariantsResponse> {
        return VariantRepos.API.getVariants(page,limit)
    }

    override suspend fun getVariant(productId: Int, variantId: Int): Response<JsonVariantResponse> {
        return VariantRepos.API.getVariant(productId,variantId)
    }

    override suspend fun searchVariant(
        query: String,
        page: Int,
        limit: Int
    ): Response<JsonVariantsResponse> {
        return VariantRepos.API.searchVariant(query,page,limit)
    }
}