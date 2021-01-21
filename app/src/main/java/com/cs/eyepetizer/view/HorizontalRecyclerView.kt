package com.cs.eyepetizer.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * 处理嵌套ViewPager时，横向滑动冲突。
 */
class HorizontalRecyclerView : RecyclerView {

    private var lastX = 0f
    private var lastY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
                if (abs(deltaX) < abs(deltaY)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {  //水平滑动

                    if (deltaX < 0) { //手指从右向左滑动

                        //recyclerview不能右滑(即手指从右向左滑动)
                        if (!canScrollHorizontally(1)) {
                            parent.requestDisallowInterceptTouchEvent(false)
                        } else {
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                    } else {  // 手指从左向右滑动

                        //recyclerview不能左滑(即手指从左向右滑动)
                        if (!canScrollHorizontally(-1)) {
                            parent.requestDisallowInterceptTouchEvent(false)
                        } else {
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                    }
                }

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {
            }
        }
        lastX = x
        lastY = y
        return super.dispatchTouchEvent(ev)
    }
}
