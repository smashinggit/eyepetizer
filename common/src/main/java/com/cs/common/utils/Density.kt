package com.cs.common.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment


fun Context.screenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.screenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

fun Context.dp2px(dp: Float): Int {
    val density = resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun Context.px2dp(px: Float): Int {
    val density = resources.displayMetrics.density
    return (px / density + 0.5f).toInt()
}

fun Fragment.dp2px(dp: Float): Int {
    val density = resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun Fragment.px2dp(px: Float): Int {
    val density = resources.displayMetrics.density
    return (px / density + 0.5f).toInt()
}

fun View.dp2px(dp: Float): Float {
    val density = context.resources.displayMetrics.density
    return dp * density + 0.5f
}

fun View.px2dp(px: Float): Int {
    val density = context.resources.displayMetrics.density
    return (px / density + 0.5f).toInt()
}

fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        android.content.res.Resources.getSystem().displayMetrics
    )
}



