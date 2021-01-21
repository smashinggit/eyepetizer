package com.cs.common.http

import com.alibaba.fastjson.JSONObject
import com.google.gson.JsonSyntaxException
import java.lang.Exception
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun JSONObject.success() = 200 == getIntValue("returnCode")
fun JSONObject.toBusinessException() = BusinessException(this)
fun Throwable.toTip(): String = ResponseHandler.getFailureTip(this)

object ResponseHandler {
    const val NETWORK_ERROR = "网络错误"
    const val NETWORK_CONNECT_ERROR = "网络连接异常"
    const val CONNECT_TIMEOUT_ERROR = "网络连接超时"
    const val NO_ROUTE_TO_HOST_ERROR = "无法连接到服务器"
    const val NETWORK_RESPONSE_CODE_ERROR = "服务器状态码异常："
    const val JSON_DATA_ERROR = "数据解析异常"
    const val UNKNOWN_ERROR = "未知错误"

    fun getFailureTip(t: Throwable): String {
        return when (t) {
            is ConnectException -> NETWORK_CONNECT_ERROR
            is SocketTimeoutException -> CONNECT_TIMEOUT_ERROR
            is NoRouteToHostException -> NO_ROUTE_TO_HOST_ERROR
            is UnknownHostException -> NETWORK_ERROR
            is JsonSyntaxException -> JSON_DATA_ERROR
            is BusinessException -> NETWORK_RESPONSE_CODE_ERROR + t.code
            else -> {
                UNKNOWN_ERROR
            }
        }
    }
}