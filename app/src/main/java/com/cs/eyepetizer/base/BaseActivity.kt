package com.cs.eyepetizer.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import com.cs.eyepetizer.R
import com.cs.eyepetizer.event.MessageEvent
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 *
 * @Author ChenSen
 * @Date 2020/5/6-21:01
 * @Desc
 *
 */
abstract class BaseActivity : AppCompatActivity(), ILoading {

    //判断当前Activity是否在前台
    protected var mIsActive: Boolean = false

    //当前Activity的实例
    protected var mInstance: Activity? = null

    //当前Activity的弱引用，防止内存泄露
    protected var mInstanceWr: WeakReference<Activity>? = null

    // Loading布局 和 Error布局
    protected var mLoadingImpl: LoadingImpl? = null


    override fun showLoading(tip: String) {
        mLoadingImpl?.showLoading(tip)
    }

    override fun hideLoading() {
        mLoadingImpl?.hideLoading()
    }

    override fun showErrorView(tip: String, block: View.() -> Unit) {
        mLoadingImpl?.showErrorView(tip, block)
    }

    override fun hideErrorView() {
        mLoadingImpl?.hideErrorView()
    }

    override fun showContentView() {
        mLoadingImpl?.showContentView()
    }

    override fun hideContentView() {
        mLoadingImpl?.hideContentView()
    }

    abstract fun setLayoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutRes())
        setStatusBarBackground(R.color.colorPrimaryDark)
        EventBus.getDefault().register(this)


        if (mLoadingImpl == null) {
            mLoadingImpl = LoadingImpl(window.decorView.findViewById(android.R.id.content))
        }
        mIsActive = true

        mInstance = this
        mInstanceWr = WeakReference(this)

        initView()
        observer()
    }

    open fun initView() {}
    open fun observer() {}


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mIsActive = false
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MessageEvent) {
    }


    /**
     * 设置状态栏背景色
     */
    fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }

}