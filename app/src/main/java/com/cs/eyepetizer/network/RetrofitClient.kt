package com.cs.eyepetizer.network

import com.cs.eyepetizer.network.intercept.BasicParamsInterceptor
import com.cs.eyepetizer.network.intercept.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var DEBUG: Boolean = false

    const val HEADER_NO_ENCRYPT = "NO_ENCRYPT"
    const val HEADER_NO_ENCRYPT_REQUEST = "NO_ENCRYPT_REQUEST"
    const val HEADER_NO_ENCRYPT_RESPONSE = "NO_ENCRYPT_RESPONSE"

    const val TEXT_PLAIN = "text/plain; charset=UTF-8"

    const val BASE_URL = "http://baobab.kaiyanapp.com/"


    val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(BasicParamsInterceptor())
            .build()
    }

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun <S> create(service: Class<S>): S {
        return retrofit.create(service)
    }

}