package com.cs.eyepetizer.utils.webview

import android.os.Bundle
import android.webkit.WebView
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionClient
import java.util.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 *
 * SonicSessionClient  is a thin API class that delegates its public API
 * to a backend WebView class instance, such as loadUrl and loadDataWithBaseUrl
 **/
class SonicSessionClientImpl : SonicSessionClient() {
    private lateinit var webView: WebView

    fun bindWebView(webView: WebView) {
        this.webView = webView
    }

    override fun bindSession(session: SonicSession?) {
        super.bindSession(session)
    }

    override fun loadDataWithBaseUrlAndHeader(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?,
        headers: HashMap<String, String>?
    ) {
        webView.loadDataWithBaseURL(baseUrl, data ?: "", mimeType, encoding, historyUrl)
    }

    override fun loadUrl(url: String, extraData: Bundle?) {
        webView.loadUrl(url)
    }

    override fun loadDataWithBaseUrl(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?
    ) {
        webView.loadDataWithBaseURL(baseUrl, data ?: "", mimeType, encoding, historyUrl)
    }


}