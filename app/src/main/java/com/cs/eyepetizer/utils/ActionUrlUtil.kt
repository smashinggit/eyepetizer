package com.cs.eyepetizer.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.cs.common.utils.toast
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.ShareDialogFragment
import java.net.URLDecoder

/**
 * actionUrl事件处理工具类。通过截取actionUrl相关信息，并进行相应事件处理。
 */
object ActionUrlUtil {

    // 处理ActionUrl事件
    fun process(context: Context, actionUrl: String?, toastTitle: String = "") {
        if (actionUrl == null) return
        val decodeUrl = URLDecoder.decode(actionUrl, "UTF-8")
        when {
            decodeUrl.startsWith(Constant.ActionUrl.WEBVIEW) -> {
//                WebViewActivity.start(
//                    activity,
//                    decodeUrl.getWebViewInfo().first(),
//                    decodeUrl.getWebViewInfo().last()
//                )
            }
            decodeUrl == Constant.ActionUrl.RANKLIST -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            decodeUrl.startsWith(Constant.ActionUrl.TAG) -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            decodeUrl == Constant.ActionUrl.HP_SEL_TAB_TWO_NEWTAB_MINUS_THREE -> {
//                EventBus.getDefault().post(SwitchPagesEvent(DailyFragment::class.java))
            }
            decodeUrl == Constant.ActionUrl.CM_TAGSQUARE_TAB_ZERO -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            decodeUrl == Constant.ActionUrl.CM_TOPIC_SQUARE -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            decodeUrl == Constant.ActionUrl.CM_TOPIC_SQUARE_TAB_ZERO -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            decodeUrl.startsWith(Constant.ActionUrl.COMMON_TITLE) -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            actionUrl == Constant.ActionUrl.HP_NOTIFI_TAB_ZERO -> {
//                EventBus.getDefault().post(SwitchPagesEvent(PushFragment::class.java))
//                EventBus.getDefault().post(RefreshEvent(PushFragment::class.java))
            }
            actionUrl.startsWith(Constant.ActionUrl.TOPIC_DETAIL) -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
            actionUrl.startsWith(Constant.ActionUrl.DETAIL) -> {
//                getConversionVideoId(actionUrl)?.run { NewDetailActivity.start(activity, this) }
            }
            else -> {
                context.toast("${toastTitle},${context.getString(R.string.currently_not_supported)}")
            }
        }
    }

    /**
     * 截取标题与url信息。
     *
     * @return first=标题 last=url
     */
    private fun String.getWebViewInfo(): Array<String> {
        val title = this.split("title=").last().split("&url").first()
        val url = this.split("url=").last()
        return arrayOf(title, url)
    }

    /**
     *  截取视频id。
     *
     *  @param actionUrl 解码后的actionUrl
     *  @return 视频id
     */
    private fun getConversionVideoId(actionUrl: String?): Long? {
        return try {
            val list = actionUrl?.split(Constant.ActionUrl.DETAIL)
            list!![1].toLong()
        } catch (e: Exception) {
            null
        }
    }
}


fun showShareDialog(activity: AppCompatActivity, shareContent: String) {
    ShareDialogFragment().showDialog(activity, shareContent)
}
