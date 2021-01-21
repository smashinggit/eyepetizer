package com.cs.common.utils

import android.content.Context
import android.content.Intent
import java.io.Serializable


inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Context.startActivity(vararg pairs: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    addParamToIntent(intent, pairs)
    startActivity(intent)
}

fun addParamToIntent(
        intent: Intent,
        pairs: Array<out Pair<String, Any>>
) {
    pairs.forEach {
        when (val second = it.second) {
            is String -> intent.putExtra(it.first, second)
            is Int -> intent.putExtra(it.first, second)
            is Boolean -> intent.putExtra(it.first, second)
            is Serializable -> intent.putExtra(it.first, second)
            is Char -> intent.putExtra(it.first, second)
            is Long -> intent.putExtra(it.first, second)
            is Float -> intent.putExtra(it.first, second)
            //todo 完善常用类型
        }
    }
}
