package com.cs.eyepetizer.view.tablayout.listener

import android.graphics.RectF

/**
 *
 * @author  ChenSen
 * @date  2021/1/21
 * @desc
 **/
interface IIndicator {

    fun moveTo(x: Int, y: Int)

    fun getIndicatorRect(): RectF

}