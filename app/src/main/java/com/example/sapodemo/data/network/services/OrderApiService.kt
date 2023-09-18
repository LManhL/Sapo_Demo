package com.example.sapodemo.data.network.services

import com.example.sapodemo.data.network.config.EndPoint
import com.example.sapodemo.data.network.model.order.JsonOrderSourceResponse
import com.example.sapodemo.data.network.model.order.OrderPost
import com.example.sapodemo.data.network.model.order.OrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderApiService {
    @Headers("X-Sapo-Client: Android","Content-Type: application/json")
    @POST(EndPoint.ORDER_END_POINT)
    suspend fun postOrder(@Header("X-Sapo-LocationId") locationId: Int, @Body requestBody: OrderPost): Response<OrderResponse>
    @GET(EndPoint.ORDER_SOURCE_END_POINT)
    suspend fun getOrderSources(): Response<JsonOrderSourceResponse>
}