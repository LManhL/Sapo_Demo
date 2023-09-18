package com.example.sapodemo.data.network.repos
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object BaseRepos {
    private const val BASE_URL = "https://mobile-test.mysapogo.com/"

    private val HTTP_CLIENT = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("X-Sapo-SessionId","ec182c9991feadd409bdac36272805fb")
            .build()
        chain.proceed(request)
    }).build()
    private val MOSHI: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val RETROFIT: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(MOSHI))
        .baseUrl(BASE_URL)
        .client(HTTP_CLIENT)
        .build()
}