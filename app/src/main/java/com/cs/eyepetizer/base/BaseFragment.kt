package com.cs.eyepetizer.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cs.common.utils.log
import com.umeng.analytics.MobclickAgent

/**
 *
 * @Author ChenSen
 * @Date 2020/5/6-21:01
 * @Desc Fragment的基类
 */
abstract class BaseFragment : Fragment(), ILoading {

    //是否已经加载过数据
    protected var mIsLoadedData = false

    protected var mRootView: View? = null

    // Loading布局 和 Error布局
    protected var mLoadingImpl: LoadingImpl? = null

    protected lateinit var mActivity: Activity


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

    //进行View相关的初始化
    protected open fun initView() {}

    //在这里可以进行LiveData的监听
    protected open fun observer() {}

    //页面首次可见时调用一次该方法，在这里可以请求网络数据等
    protected open fun onFirstVisible() {}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(setLayoutRes(), container, false).also {
            mLoadingImpl = LoadingImpl(it)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        observer()
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        //当Fragment在屏幕上可见并且没有加载过数据时调用
        if (!mIsLoadedData) {
            mIsLoadedData = true
            onFirstVisible()
        }

        MobclickAgent.onPageStart(this.javaClass.name)
        log("onPageStart ${this.javaClass.name}")
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(this.javaClass.name)
        log("onPageEnd ${this.javaClass.name}")
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

}