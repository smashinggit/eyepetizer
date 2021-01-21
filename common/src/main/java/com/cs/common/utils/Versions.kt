package com.cs.common.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc : 用于获取当前软件的相关信息
 **/
object Versions {
    private var TAG = "Versions"

    //包名
    fun packageName(context: Context): String {
        return context.packageName
    }

    //应用程序的名字
    fun applicationName(context: Context): String {
        return context.resources.getString(context.applicationInfo.labelRes)
    }

    //获取当前应用程序的版本名
    fun appVersionName(context: Context): String {
        return context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }

    //获取当前应用程序的版本号
    fun appVersionCode(context: Context): Long {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toLong()
        }
    }


    /**
     * 判断某个应用是否安装。
     * 要检查是否安装的应用包名
     * @return 安装返回true，否则返回false。
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo != null
    }

    /**
     * 判断手机是否安装了QQ。
     */
    fun isQQInstalled(context: Context) = isInstalled(context, "com.tencent.mobileqq")

    /**
     * 判断手机是否安装了微信。
     */
    fun isWeChatInstalled(context: Context) = isInstalled(context, "com.tencent.mm")

    /**
     * 判断手机是否安装了微博。
     * */
    fun isWeiBoInstalled(context: Context) = isInstalled(context, "com.sina.weibo")

}
