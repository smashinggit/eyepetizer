package com.cs.eyepetizer.network.intercept

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc
 **/
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val newRequest = request.newBuilder().apply {
            header("model", "Android")
            header("If-Modified-Since", "${Date()}")
            header("User-Agent", System.getProperty("http.agent") ?: "unknown")
        }.build()

        return chain.proceed(newRequest)
    }
}