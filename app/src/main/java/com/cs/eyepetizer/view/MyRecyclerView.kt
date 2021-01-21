package com.cs.eyepetizer.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc
 *
 *  判断是否滑动到底部， canKeepSlidingForVertically(1);返回false表示不能往上滑动
 *  判断是否滑动到顶部， canKeepSlidingForVertically(-1);返回false表示不能往下滑动
 **/
class MyRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    //检查此视图是否可以在某个方向上垂直滚动,
    //@param direction负值检查向上滚动，正向检查向下滚动。
    //@return如果此视图可以在指定方向滚动，则为true，否则为false
    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollVertically(direction)
    }



    override fun canScrollHorizontally(direction: Int): Boolean {
        return super.canScrollHorizontally(direction)
    }
}