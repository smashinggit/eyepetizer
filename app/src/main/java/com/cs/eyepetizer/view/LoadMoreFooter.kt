package com.cs.eyepetizer.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.cs.common.utils.log
import com.cs.eyepetizer.R
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent
import kotlinx.android.synthetic.main.layout_refresh_footor.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/19
 * @desc SmartRefreshLayout自定义Footer
 **/
class LoadMoreFooter : SimpleComponent, RefreshFooter {
    var REFRESH_FOOTER_PULLING = "上拉加载更多"
    var REFRESH_FOOTER_RELEASE = "释放立即加载"
    var REFRESH_FOOTER_LOADING = "正在加载..."
    var REFRESH_FOOTER_REFRESHING = "正在刷新..."
    var REFRESH_FOOTER_FINISH = "加载完成"
    var REFRESH_FOOTER_FAILED = "加载失败"

    //    var REFRESH_FOOTER_NOTHING = "没有更多数据了"
    var REFRESH_FOOTER_NOTHING = "The End"


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
        View.inflate(context, R.layout.layout_refresh_footor, this)
    }

    override fun onAnimationStart() {
        super.onAnimationStart()
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        if (success) {
            tvTitle.text = REFRESH_FOOTER_FINISH
        } else {
            tvTitle.text = REFRESH_FOOTER_FAILED
        }
        return 500  //
    }

    /**
     * 【仅限框架内调用】设置数据全部加载完成，将不能再次触发加载功能
     * @param noMoreData 是否有更多数据
     * @return true 支持全部加载完成的状态显示 false 不支持
     */
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (noMoreData) {
            tvTitle.visibility = View.INVISIBLE
            tvNoMore.visibility = View.VISIBLE
            tvNoMore.text = REFRESH_FOOTER_NOTHING
//            tvNoMore.typeface = Typeface.createFromAsset(context.assets, "/fonts/Lobster-1.4.otf")
        }
        return true
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.None -> {
            }

            RefreshState.PullUpToLoad -> {
                tvTitle.text = REFRESH_FOOTER_PULLING
            }
            RefreshState.ReleaseToLoad -> {
                tvTitle.text = REFRESH_FOOTER_PULLING
            }
            RefreshState.Loading -> {
                tvTitle.text = REFRESH_FOOTER_LOADING
            }
        }
    }

    fun setTitleColor(color: Int) {
        tvTitle.setTextColor(color)
    }

    fun setNoMoreTipColor(color: Int) {
        tvNoMore.setTextColor(color)
    }

}