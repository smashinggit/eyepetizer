package com.cs.eyepetizer.network.intercept

import android.os.Build
import com.cs.common.utils.Devices
import com.cs.common.utils.Versions
import com.cs.common.utils.screenHeight
import com.cs.common.utils.screenWidth
import com.cs.eyepetizer.App
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc
 **/
class BasicParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url.newBuilder().apply {
            addQueryParameter("udid", Devices.deviceSerial(App.INSTANCE))
            addQueryParameter("deviceModel", Devices.deviceModel())
            addQueryParameter("first_channel", Devices.deviceBrand())
            addQueryParameter("last_channel", Devices.deviceBrand())
            addQueryParameter("vc", "630012")
            addQueryParameter("vn", "6.3.01")
            addQueryParameter(
                "size",
                "${App.INSTANCE.screenWidth()}X${App.INSTANCE.screenHeight()}"
            )
            addQueryParameter("system_version_code", "${Build.VERSION.SDK_INT}")
        }.build()
        return chain.proceed(
            request.newBuilder().url(url).method(request.method, request.body).build()
        )
    }
}