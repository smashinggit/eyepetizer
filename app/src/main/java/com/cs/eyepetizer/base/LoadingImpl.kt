package com.cs.eyepetizer.base

import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.cs.common.utils.gone
import com.cs.common.utils.visible
import com.cs.eyepetizer.R

/**
 *
 * @author  ChenSen
 * @date  2021/1/8
 * @desc 管理 Loading 布局、Error 布局
 **/

class LoadingImpl(private val rootView: View) : ILoading {

    private var mErrorView: View? = null  //由于服务器或网络异常导致加载失败显示的布局
    private var mLoadingView: ProgressBar? = null

    override fun showLoading(tip: String) {
        if (mLoadingView == null) {
            mLoadingView = rootView.findViewById(R.id.loading)
        }
        mLoadingView?.visible()
    }

    override fun hideLoading() {
        mLoadingView?.gone()
    }

    override fun showErrorView(tip: String, block: View.() -> Unit) {
        if (mErrorView == null) {
            val viewStub = rootView.findViewById<ViewStub>(R.id.layout_loaded_error_holder)
            viewStub?.inflate()?.apply {
                mErrorView = this

                findViewById<TextView>(R.id.errorText)?.apply {
                    text = tip
                }

                findViewById<Button>(R.id.retry)?.apply {
                    setOnClickListener { block.invoke(this) }
                }

                setOnClickListener {
                    block(it)
                }
            }
        }
        mErrorView?.visible()
    }

    override fun hideErrorView() {
        mErrorView?.gone()
    }

    override fun showContentView() {
        rootView.visible()
        hideErrorView()
    }

    override fun hideContentView() {
        rootView.gone()
    }


}