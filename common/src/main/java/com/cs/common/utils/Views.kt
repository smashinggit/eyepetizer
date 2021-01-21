package com.cs.common.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation


var enable = true
fun View.setOnValidClickListener(onClick: (View) -> Unit) {
    setOnClickListener {
        if (enable) {
            enable = false
            onClick(it)
        }

        postDelayed({
            enable = true
        }, 300)
    }
}

fun setOnClickListener(vararg views: View, block: View.() -> Unit) {
    val listener = View.OnClickListener {
        block(it)
    }
    views.forEach {
        it.setOnClickListener(listener)
    }
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.goneWithAlpha(duration: Long = 500L) {
    this?.animation?.cancel()
    this?.startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        fillAfter = true
    })
    this?.visibility = View.GONE
}

fun View?.visibleWithAlpha(duration: Long = 500L) {
    this?.animation?.cancel()
    this?.startAnimation(AlphaAnimation(0f, 1f).apply {
        this.duration = duration
        fillAfter = true
    })
    this?.visibility = View.VISIBLE
}

fun View?.invisibleWithAlpha(duration: Long = 500L) {
    this?.animation?.cancel()
    this?.visibility = View.INVISIBLE
    this?.startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        fillAfter = true
    })
}

fun Int.inflate(parent: ViewGroup, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(parent.context).inflate(this, parent, attachToRoot)
}

