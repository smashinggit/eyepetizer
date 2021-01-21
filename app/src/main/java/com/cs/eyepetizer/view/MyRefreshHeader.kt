package com.cs.eyepetizer.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.cs.common.utils.dp2px
import com.cs.eyepetizer.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent
import kotlinx.android.synthetic.main.layout_refresh_header.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/19
 * @desc SmartRefreshLayout自定义Header
 **/
class MyRefreshHeader : SimpleComponent, RefreshHeader {

    var REFRESH_HEADER_PULLING = "下拉刷新"
    var REFRESH_HEADER_RELEASE = "释放立即刷新"
    var REFRESH_HEADER_LOADING = "正在加载..."
    var REFRESH_HEADER_FINISH = "刷新成功"
    var REFRESH_HEADER_FAILED = "刷新失败"


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.layout_refresh_header, this)
        minimumHeight = dp2px(80f).toInt()
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        super.onStartAnimator(refreshLayout, height, maxDragHeight)
        pb.visibility = View.VISIBLE
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        pb.visibility = View.INVISIBLE

        tvTitle.text = if (success) {
            REFRESH_HEADER_FINISH
        } else {
            REFRESH_HEADER_FAILED
        }
        super.onFinish(refreshLayout, success)
        return 500  //延迟500毫秒之后再弹回
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.PullDownToRefresh -> { //下拉过程
                tvTitle.text = REFRESH_HEADER_PULLING
            }
            RefreshState.ReleaseToRefresh -> {  //松开刷新
                tvTitle.text = REFRESH_HEADER_RELEASE
            }
            RefreshState.Refreshing -> {  //loading中
                tvTitle.text = REFRESH_HEADER_LOADING;
            }
        }
    }


}