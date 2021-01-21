package com.cs.eyepetizer.view.tablayout

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.cs.common.utils.dp2px
import com.cs.eyepetizer.R
import com.cs.eyepetizer.view.tablayout.listener.ITabView
import kotlinx.android.synthetic.main.item_tab_common.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc 代表一个标签视图
 **/
class TabView : FrameLayout, ITabView {

    // Tab
    private val mTabPaddingLeft = dp2px(5f).toInt()
    private val mTabPaddingTop = dp2px(0f).toInt()
    private val mTabPaddingRight = dp2px(5f).toInt()
    private val mTabPaddingBottom = dp2px(0f).toInt()

    // Title
    private val TEXT_BOLD_NONE = 0
    private val TEXT_BOLD_WHEN_SELECT = 1
    private val TEXT_BOLD_BOTH = 2

    private var mTitleSize = dp2px(6f)
    private var mTitleSelectColor = Color.BLACK
    private var mTitleUnSelectColor = Color.LTGRAY
    private var mTextBold = TEXT_BOLD_NONE  //是否加粗
    private var mTextAllCaps = false


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
        View.inflate(context, R.layout.item_tab_common, this)
    }

    override fun onSelect(position: Int) {
        tvTitle.setTextColor(mTitleSelectColor)
    }

    override fun onReSelect(position: Int) {

    }

    override fun onUnSelect(position: Int) {
        tvTitle.setTextColor(mTitleUnSelectColor)
    }


    override fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun setSelectTitleColor(color: Int) {
        mTitleSelectColor = color
    }

    override fun setUnSelectTitleColor(color: Int) {
        mTitleUnSelectColor = color
    }

}