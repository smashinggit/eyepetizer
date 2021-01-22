package com.cs.eyepetizer.view.tablayout

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.cs.common.utils.dp2px
import com.cs.common.utils.log
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

    companion object {
        const val TEXT_BOLD_NONE = 0
        const val TEXT_BOLD_WHEN_SELECT = 1
        const val TEXT_BOLD_BOTH = 2
    }

    // Tab
    private val mTabPaddingLeft = dp2px(5f).toInt()
    private val mTabPaddingTop = dp2px(0f).toInt()
    private val mTabPaddingRight = dp2px(5f).toInt()
    private val mTabPaddingBottom = dp2px(0f).toInt()

    private var mTitleSelectColor = Color.BLACK
    private var mTitleUnSelectColor = Color.LTGRAY
    private var mTextBold = TEXT_BOLD_NONE  //是否加粗
    private var mTextAllCaps = false

    private var mIsSelected = false
    private var mTitleBound = TEXT_BOLD_WHEN_SELECT

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
        mIsSelected = true
        tvTitle.apply {
            setTextColor(mTitleSelectColor)
            tvTitle.paint.isFakeBoldText = mTitleBound != TEXT_BOLD_NONE
        }
    }

    override fun onReSelect(position: Int) {

    }

    override fun onUnSelect(position: Int) {
        mIsSelected = false
        tvTitle.apply {
            setTextColor(mTitleUnSelectColor)
            tvTitle.paint.isFakeBoldText = mTitleBound != TEXT_BOLD_BOTH
        }
    }


    override fun setTitleBoundStyle(style: Int) {
        this.mTitleBound = style
    }

    override fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun setTitleSize(size: Float) {
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    override fun setSelectTitleColor(color: Int) {
        mTitleSelectColor = color
    }

    override fun setUnSelectTitleColor(color: Int) {
        mTitleUnSelectColor = color
    }

}