package com.cs.eyepetizer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cs.common.utils.Versions
import com.cs.eyepetizer.App
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity

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
 *  4.Activity销毁的时候，应该先在父布局中移除webview，再调用后续处理方法
 *  5.Activity onDestory最后，需要手动调用杀死进程的方法
 *
 *
 *  对于Android调用JS代码的方法有2种：
 *  1. 通过WebView的 loadUrl（）
 *  2. 通过WebView的 evaluateJavascript（）
 *
 * 对于JS调用Android代码的方法有3种：
 * 1. 通过WebView的addJavascriptInterface（）进行对象映射
 * 2. 通过 WebViewClient 的shouldOverrideUrlLoading ()方法回调拦截 url
 * 3. 通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息
 *
 **/
class WebViewActivity : BaseActivity() {


    override fun setLayoutRes() = R.layout.activity_webview


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra(TITLE) ?: Versions.applicationName(this)
        val url = intent.getStringExtra(URL) ?: DEFAULT_URL
        val isShare = intent.getBooleanExtra(IS_SHARE, false)
        val isTitleFixed = intent.getBooleanExtra(IS_TITLE_FIXED, false)
        val mode = intent.getIntExtra(MODE, MODE_DEFAULT)

        val webViewFragment = WebViewFragment.create(title, url, isShare, isTitleFixed, mode)

        supportFragmentManager.beginTransaction().add(R.id.container, webViewFragment)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if ((supportFragmentManager.findFragmentById(R.id.container) as WebViewFragment).goBack()) {
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //关于WebView的释放代码在WebViewFragment中
    }

    companion object {

        private const val URL = "url"
        private const val TITLE = "title"
        private const val IS_SHARE = "is_share"
        private const val IS_TITLE_FIXED = "isTitleFixed"
        private const val MODE = "mode"
        const val DEFAULT_URL = "https://github.com/VIPyinzhiwei/Eyepetizer"
        val DEFAULT_TITLE = Versions.applicationName(App.INSTANCE)


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
         *                      MODE_DEFAULT 使用WebView加载；
         *                      MODE_SONIC 使用VasSonic框架加载；
         *                      MODE_SONIC_WITH_OFFLINE_CACHE 使用VasSonic框架离线加载
         */
        fun start(
            context: Context,
            title: String,
            url: String,
            isShare: Boolean = true,
            isTitleFixed: Boolean = true,
            mode: Int = MODE_SONIC
        ) {
            val intent = Intent(context, WebViewActivity::class.java).apply {
                putExtra(TITLE, title)
                putExtra(URL, url)
                putExtra(IS_SHARE, isShare)
                putExtra(IS_TITLE_FIXED, isTitleFixed)
                putExtra(MODE, mode)
//                putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis())
            }
            context.startActivity(intent)
        }
    }
}