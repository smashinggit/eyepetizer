package com.cs.common.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * 时间和日期工具类。
 */
object Dates {

    const val VALUE_MINUTE = (60 * 1000).toLong()
    const val VALUE_HOUR = 60 * VALUE_MINUTE
    const val VALUE_DAY = 24 * VALUE_HOUR
    const val VALUE_WEEK = 7 * VALUE_DAY
    const val VALUE_MONTH = 4 * VALUE_WEEK
    const val VALUE_YEAR = 365 * VALUE_MONTH

    const val YEAR = "年"
    const val MONTH = "个月"
    const val DAY = "天"
    const val HOUR = "小时"
    const val MONUTE = "分钟"

    const val MINUTES_AGO = "分钟前"
    const val HOURS_AGO = "小时前"
    const val DAYS_AGO = "天前"
    const val WEEKS_AGO = "周前"


    /**
     * 根据传入的Unix时间戳，获取转换过后更加易读的时间格式。
     * @param dateMillis
     * Unix时间戳
     * @return 转换过后的时间格式，如2分钟前，1小时前。
     */
    fun getConvertedDate(dateMillis: Long): String {
        val currentMillis = System.currentTimeMillis()
        val timePast = currentMillis - dateMillis
        if (timePast > -VALUE_MINUTE) { // 采用误差一分钟以内的算法，防止客户端和服务器时间不同步导致的显示问题
            when {
                /*timePast < HOUR -> {
                    var pastMinutes = timePast / MINUTE
                    if (pastMinutes <= 0) {
                        pastMinutes = 1
                    }
                    return pastMinutes.toString() + GlobalUtil.getString(R.string.minutes_ago)
                }*/
                timePast < VALUE_DAY -> {
                    var pastHours = timePast / VALUE_HOUR
                    if (pastHours <= 0) {
                        pastHours = 1
                    }
                    /*return pastHours.toString() + GlobalUtil.getString(R.string.hours_ago)*/
                    return getDateAndHourMinuteTime(dateMillis)
                }
                timePast < VALUE_WEEK -> {
                    var pastDays = timePast / VALUE_DAY
                    if (pastDays <= 0) {
                        pastDays = 1
                    }
                    return "$pastDays $DAYS_AGO}"
                }
                timePast < VALUE_MONTH -> {
                    var pastDays = timePast / VALUE_WEEK
                    if (pastDays <= 0) {
                        pastDays = 1
                    }
                    return "$pastDays $WEEKS_AGO}"
                }
                else -> return getDate(dateMillis)
            }
        } else {
            return getDateAndTime(dateMillis)
        }
    }

    fun isBlockedForever(timeLeft: Long) = timeLeft > 5 * VALUE_YEAR

     fun getDate(dateMillis: Long, pattern: String = "yyyy-MM-dd"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }

    private fun getDateAndTime(dateMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }

    private fun getDateAndHourMinuteTime(dateMillis: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(dateMillis))
    }

}

/**
 * 获取转换后的时间样式。
 *
 * @return 处理后的时间样式，示例：06:50
 */
fun Int.conversionVideoDuration(): String {
    val minute = 1 * 60
    val hour = 60 * minute
    val day = 24 * hour

    return when {
        this < day -> {
            String.format("%02d:%02d", this / minute, this % 60)
        }
        else -> {
            String.format("%02d:%02d:%02d", this / hour, (this % hour) / minute, this % 60)
        }
    }
}