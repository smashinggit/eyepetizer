package com.cs.eyepetizer.view.tablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.cs.common.utils.dp2px
import com.cs.eyepetizer.view.tablayout.listener.IIndicator

/**
 *
 * @author  ChenSen
 * @date  2021/1/21
 * @desc
 **/
class LineIndicator : View, IIndicator {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    var mIndicatorColor = Color.BLACK

    private val mRect = RectF(0f, 0f, dp2px(50f), dp2px(5f))
    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = mIndicatorColor
    }

    private fun initView(context: Context) {


    }


    override fun onDraw(canvas: Canvas) {
    }

    override fun moveTo(x: Int, y: Int) {
    }

    override fun getIndicatorRect(): RectF {
        return mRect
    }

}