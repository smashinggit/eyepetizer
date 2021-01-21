package com.cs.eyepetizer.utils.webview

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.WebResourceResponse
import com.cs.common.utils.log
import com.cs.eyepetizer.BuildConfig
import com.tencent.sonic.sdk.SonicRuntime
import com.tencent.sonic.sdk.SonicSessionClient
import java.io.File
import java.io.InputStream

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc the sonic host application must implement SonicRuntime to do right things.
 **/
class SonicRuntimeImpl(context: Context) : SonicRuntime(context) {

    override fun showToast(text: CharSequence?, duration: Int) {
    }

    override fun log(tag: String?, level: Int, message: String?) {
        if (BuildConfig.DEBUG) {
            context.log(message ?: "")
        }
    }

    /**
     * 获取用户UA信息
     */
    override fun getUserAgent(): String {
        return System.getProperty("http.agent") ?: "unknown"
    }

    override fun isNetworkValid(): Boolean {
        return true
    }

    override fun postTaskToThread(task: Runnable, delayMillis: Long) {
        val thread = Thread(task, "SonicThread")
        thread.start()
    }

    override fun isSonicUrl(url: String?): Boolean {
        return true
    }

    override fun setCookie(url: String?, cookies: MutableList<String>?): Boolean {
        return if (!url.isNullOrEmpty() && !cookies.isNullOrEmpty()) {
            val cookieManager = CookieManager.getInstance()
            cookies.forEach {
                cookieManager.setCookie(url, it)
            }
            true
        } else {
            false
        }
    }

    override fun getCookie(url: String?): String {
        val cookieManager = CookieManager.getInstance()
        return cookieManager.getCookie(url)
    }

    override fun createWebResourceResponse(
        mimeType: String?,
        encoding: String?,
        data: InputStream?,
        headers: MutableMap<String, String>?
    ): Any {
        val resourceResponse = WebResourceResponse(mimeType, encoding, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.responseHeaders = headers
        }
        return resourceResponse
    }

    /**
     * 获取用户ID信息
     */
    override fun getCurrentUserAccount(): String {
        return ""
    }

    override fun notifyError(client: SonicSessionClient?, url: String?, errorCode: Int) {
    }

}