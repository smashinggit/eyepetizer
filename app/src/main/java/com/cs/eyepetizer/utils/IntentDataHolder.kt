package com.cs.eyepetizer.utils

import java.lang.ref.WeakReference

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc 数据传输工具类，处理Intent携带大量数据时，超过1MB限制出现的异常场景
 **/
object IntentDataHolder {

    //存放数据
    private val mDataList = hashMapOf<String, WeakReference<Any>>()


    fun setData(key: String, value: Any) {
        mDataList[key] = WeakReference(value)
    }


    fun <T> getData(key: String): T? {

        val weakReference = mDataList[key]
        return try {
            weakReference?.get() as T
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}