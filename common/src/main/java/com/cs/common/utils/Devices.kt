package com.cs.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.util.*


/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc : 用于获取设备的硬件信息
 **/

object Devices {



    //获取设备的的型号，如果无法获取到，则返回Unknown。
    fun deviceModel(): String {
        return if (Build.MODEL.isNullOrEmpty()) {
            "unknown"
        } else {
            Build.MODEL
        }
    }

    //获取设备的的品牌，如果无法获取到，则返回Unknown。
    fun deviceBrand(): String {
        return if (Build.BRAND.isNullOrEmpty()) {
            "unknown"
        } else {
            Build.BRAND
        }
    }


    private var mSerial = ""

    // 获取设备的序列号
    // 如果无法获取到设备的序列号，则
    // 会生成一个随机的UUID来作为设备的序列号(google推荐做法)
    // 并将该序列号保存到本地
    fun deviceSerial(context: Context): String {
        synchronized(this) {
            if (mSerial.isNotEmpty()) {
                return mSerial
            }

            mSerial = Files.read(context.externalCacheDir!!.absolutePath + "/serial.txt")

            if (mSerial.isEmpty()) {
                mSerial = UUID.randomUUID().toString();
                Files.save(context.externalCacheDir!!.absolutePath + "/serial.txt", mSerial)
            }
            return mSerial
        }
    }

    /**
     * 获取AndroidManifest.xml文件中，<application>标签下的meta-data值。
     *  <application>标签下的meta-data健
     */
    fun getApplicationMetaData(context: Context, key: String): String {
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            applicationInfo.metaData.getString(key) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


}