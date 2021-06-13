package com.interview.codingchallenge.networking

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.interview.codingchallenge.networking.ApiService
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.flickr.com/services/rest/"
private const val CONNECTION_TIMEOUT_MS: Long = 10

object WebClient {
    val client: ApiService by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder().connectTimeout(
                    CONNECTION_TIMEOUT_MS,
                    TimeUnit.SECONDS
                ).addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
            )
            .build()
            .create(ApiService::class.java)
    }
}
