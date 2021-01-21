package com.cs.eyepetizer.base

import android.view.View

/**
 *
 * author : ChenSen
 * date : 2021/1/8
 * desc :
 **/
interface ILoading {

    fun showLoading(tip: String = "")
    fun hideLoading()

    fun showErrorView(tip: String = "数据加载失败", block: View.() -> Unit)
    fun hideErrorView()

    fun showContentView()
    fun hideContentView()

}