package com.cs.eyepetizer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.cs.common.utils.log
import com.cs.common.utils.visible
import com.cs.eyepetizer.App
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.utils.webview.OfflinePkgSessionConnection
import com.cs.eyepetizer.utils.webview.SonicRuntimeImpl
import com.cs.eyepetizer.utils.webview.SonicSessionClientImpl
import com.tencent.sonic.sdk.*
import kotlinx.android.synthetic.main.fragment_webview.*
import kotlinx.android.synthetic.main.layout_title.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/15
 * @desc
 *
 *  防止WebView内存泄漏的方法:
 *  1.WebView所在的activity，应该在独立进程
 *  2.WebView不要再XML中声明，而应该在代码中动态添加
 *  3.实例化WebView的时候不要传activity，应该传applicationContext
 *  4.Activity销毁的时候，应该先在父布局中移除 WebView，再调用后续处理方法
 *  5.Activity onDestory最后，需要手动调用杀死进程的方法
 *
 *  对于Android调用JS代码的方法有2种：
 *  1. 通过WebView的 loadUrl（）
 *  2. 通过WebView的 evaluateJavascript（）
 *
 * 对于JS调用Android代码的方法有3种：
 * 1. 通过WebView的addJavascriptInterface（）进行对象映射
 * 2. 通过 WebViewClient 的 shouldOverrideUrlLoading ()方法回调拦截 url
 * 3. 通过 WebChromeClient 的 onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息
 *
 **/
class WebViewFragment private constructor() : BaseFragment() {

    private var mUrl = ""
    private var mTitle = ""
    private var mTitleFixed = false
    private var mIsShare = false

    private var mMode = MODE_DEFAULT
    private var mSonicSession: SonicSession? = null
    private val mWebView by lazy {

        WebView(this.requireContext().applicationContext).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun setLayoutRes() = R.layout.fragment_webview


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {

        tvTitle.text = mTitle
        if (mIsShare) ivShare.visible()

        ivShare.setOnClickListener {
//            showDialogShare("${title}:${linkUrl}")
        }
        webViewContainer.addView(mWebView)

        initWebView()
        preloadInitVasSonic()
    }

    /**
     * 使用VasSonic框架提升H5首屏加载速度
     */
    private fun preloadInitVasSonic() {
        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(
                SonicRuntimeImpl(App.INSTANCE),
                SonicConfig.Builder().build()
            )
        }

        // if it's sonic mode , startup sonic session at first time
        if (MODE_DEFAULT != mMode) { // sonic mode

            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            // if it's offline pkg mode, we need to intercept the session connection
            if (MODE_SONIC_WITH_OFFLINE_CACHE == mMode) {
                sessionConfigBuilder.setCacheInterceptor(object : SonicCacheInterceptor(null) {
                    override fun getCacheData(session: SonicSession): String? {
                        return null // offline pkg does not need cache
                    }
                })
                sessionConfigBuilder.setConnectionInterceptor(object :
                    SonicSessionConnectionInterceptor() {
                    override fun getConnection(
                        session: SonicSession,
                        intent: Intent
                    ): SonicSessionConnection {
                        return OfflinePkgSessionConnection(requireContext(), session, intent)
                    }
                })
            }

            // create sonic session and run sonic flow
            mSonicSession =
                SonicEngine.getInstance().createSession(mUrl, sessionConfigBuilder.build())
            if (null != mSonicSession) {
                mSonicSession?.bindClient(SonicSessionClientImpl())
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
                log("${mTitle},${mUrl}:create sonic session fail!")
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initWebView() {

        mWebView.settings.apply {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            javaScriptEnabled = true
            allowContentAccess = true
            databaseEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            defaultTextEncodingName = "UTF-8"
            setSupportZoom(true)
        }

        mWebView.webViewClient = MyWebViewClient()
        mWebView.webChromeClient = MyWebChromeClient()

        mWebView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            // 调用系统浏览器下载
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }


    override fun onFirstVisible() {
        if (mSonicSession != null && mSonicSession?.sessionClient != null) {
            (mSonicSession?.sessionClient as SonicSessionClientImpl).run {
                //Client ready means it's webview has been initialized,
                // can start load url or load data.
                bindWebView(mWebView)
                clientReady()
                log("mSonicSessionClient clientReady()")
            }
        } else {
            mWebView.loadUrl(mUrl)
        }
    }

    inner class MyWebViewClient : WebViewClient() {

        //Give the host application a chance to take control
        //when a URL is about to be loaded in the current WebView
        //true causes the current WebView to abort loading the URL
        //false causes the WebView to continue loading the URL as usual.
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            log("WebViewFragment shouldOverrideUrlLoading ${request?.url}")
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE

            mSonicSession?.run {
                sessionClient.pageFinish(url)
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {

            // Call sessionClient.requestResource when host allow the application to return the local data
            if (mSonicSession != null) {
                val requestResponse =
                    mSonicSession?.sessionClient?.requestResource(request?.url.toString())
                if (requestResponse is WebResourceResponse)
                    return requestResponse
            }
            return null
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {

        override fun onReceivedTitle(view: WebView?, title: String?) {
            mTitle = title ?: ""
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
        }

        /**
         * Notify the host application that the web page wants to display a
         *  JavaScript {@code alert()} dialog
         *
         * 返回值
         * false 代表 展示网页弹框，js代码被中断直到弹框消失
         * true 代表自定义操作，网页上的弹框不会展示，js代码被中断。 通过调用 result.confirm() 使js代码继续执行
         *
         */
        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return super.onJsAlert(view, url, message, result)
        }

        /**
         * Notify the host application that the web page wants to display a
         * JavaScript {@code confirm()} dialog.
         *
         * 返回值
         * false 代表 展示网页弹框，js代码被中断直到弹框消失,
         * true 代表自定义操作，网页上的弹框不会展示，js代码被中断。
         *  通过调用 result.confirm() 或者 result.cancel() 使js代码继续执行
         *
         *
         */
        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            return super.onJsConfirm(view, url, message, result)
        }

        /**
         * Notify the host application that the web page wants to display a
         * JavaScript {@code prompt()} dialog.
         *
         */
        override fun onJsPrompt(
            view: WebView?,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult?
        ): Boolean {
            return super.onJsPrompt(view, url, message, defaultValue, result)
        }
    }

    override fun onDestroyView() {
        webViewContainer.removeView(mWebView)

        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        mWebView.run {
            stopLoading()
            settings.javaScriptEnabled = false
            clearHistory()
            removeAllViews()
            destroy()
        }

        mSonicSession?.destroy()
        mSonicSession = null


        super.onDestroyView()
//        android.os.Process.killProcess(android.os.Process.myPid())
    }


    companion object {

        const val MODE_DEFAULT = 0
        const val MODE_SONIC = 1
        const val MODE_SONIC_WITH_OFFLINE_CACHE = 2

        /**
         * 跳转WebView网页界面
         *
         * @param context       上下文环境
         * @param title         标题
         * @param url           加载地址
         * @param isShare       是否显示分享按钮
         * @param isTitleFixed  是否固定显示标题，不会通过动态加载后的网页标题而改变
         * @param mode          加载模式：
         *                      MODE_DEFAULT 默认使用WebView加载；
         *                      MODE_SONIC 使用VasSonic框架加载；
         *                      MODE_SONIC_WITH_OFFLINE_CACHE 使用VasSonic框架离线加载
         */
        fun create(
            title: String,
            url: String,
            isShare: Boolean = true,
            isTitleFixed: Boolean = true,
            mode: Int = MODE_DEFAULT
        ): WebViewFragment {

            return WebViewFragment().apply {
                mUrl = url
                mTitle = title
                mIsShare = isShare
                mTitleFixed = isTitleFixed
                mMode = mode
            }
        }
    }

    fun goBack(): Boolean {
        return if (mWebView.canGoBack()) {
            mWebView.goBack()
            true
        } else {
            false
        }
    }

}