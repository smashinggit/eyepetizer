package com.cs.common.utils

import android.app.Activity
import android.content.Intent
import com.cs.common.R

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc 分享工具类
 **/

object Shares {

    const val SHARE_MORE = 0
    const val SHARE_QQ = 1
    const val SHARE_WECHAT = 2
    const val SHARE_WEIBO = 3
    const val SHARE_QQZONE = 4
    const val SHARE_WECHAT_MEMORIES = 5


    /**
     * 调用系统原生分享
     */
    fun share(activity: Activity, shareContent: String, shareType: Int) {
        processShare(activity, shareContent, shareType)
    }


    private fun processShare(activity: Activity, shareContent: String, shareType: Int) {

        when (shareType) {
            SHARE_QQ -> {
                if (!Versions.isQQInstalled(activity)) {
                    activity.toast("您的手机还没有安装QQ")
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.JumpActivity"
                )
            }
            SHARE_WECHAT -> {
                if (!Versions.isWeChatInstalled(activity)) {
                    activity.toast("您的手机还没有安装微信")
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.tencent.mm",
                    "com.tencent.mm.ui.tools.ShareImgUI"
                )
            }
            SHARE_WEIBO -> {
                if (!Versions.isWeChatInstalled(activity)) {
                    activity.toast("您的手机还没有安装微博")
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.sina.weibo",
                    "com.sina.weibo.composerinde.ComposerDispatchActivity"
                )
            }
            SHARE_QQZONE -> {
                activity.toast("该功能暂未实现，敬请期待")
            }

            SHARE_MORE -> {
                share(activity, shareContent)
            }
        }

    }


    private fun share(activity: Activity, shareContent: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareContent)
        }
        activity.startActivity(Intent.createChooser(shareIntent, "分享到"))
    }

    private fun share(
        activity: Activity,
        shareContent: String,
        packageName: String,
        className: String
    ) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareContent)
                setClassName(packageName, className)
            }
            activity.startActivity(shareIntent)
        } catch (e: Exception) {
            activity.toast("分享出现未知异常")
        }
    }
}
