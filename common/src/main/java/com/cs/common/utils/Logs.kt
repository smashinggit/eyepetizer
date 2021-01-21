package com.cs.common.utils

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

object Logs {

    private const val TAG = "tag"
    private var OPEN = true //测试包打开日志

    fun logv(msg: String, tag: String = TAG) {
        if (OPEN) {
            Log.v(tag, msg)
        }
    }

    fun logd(msg: String, tag: String = TAG) {
        if (OPEN) {
            Log.d(tag, msg)
        }
    }

    fun logi(msg: String, tag: String = TAG) {
        if (OPEN) {
            Log.i("tag", msg)
        }
    }

    fun logw(msg: String, tag: String = TAG) {
        if (OPEN) {
            Log.w("tag", msg)
        }
    }

    fun loge(msg: String, tag: String = TAG) {
        if (OPEN) {
            Log.e("tag", msg)
        }
    }

    fun logForever(msg: String, tag: String = TAG) {
        Log.e("tag", msg)
    }

}

fun Context.log(msg: String) {
    Logs.loge(msg)
}

fun Fragment.log(msg: String) {
    Logs.loge(msg)
}

fun View.log(msg: String) {
    Logs.loge(msg)
}


