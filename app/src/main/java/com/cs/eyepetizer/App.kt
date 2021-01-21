package com.cs.eyepetizer

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import com.alibaba.fastjson.JSONObject
import com.cs.common.utils.log
import com.cs.eyepetizer.view.LoadMoreFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.commonsdk.statistics.common.DeviceConfig

class App : Application() {

    companion object {
        lateinit var INSTANCE: Application
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        initRefreshLayout()
        initUM()
    }

    private fun initUM() {

        UMConfigure.setLogEnabled(BuildConfig.DEBUG)

        UMConfigure.init(
            this,
            Constant.UMeng.APP_KEY,
            "test",
            UMConfigure.DEVICE_TYPE_PHONE,
            ""
        )

        //选择AUTO页面采集模式，统计SDK基础指标无需手动埋点可自动采集
        //建议在宿主App的Application.onCreate函数中调用此函数
        //如果您选择的模式是AUTO，初始化函数是默认对所有activity自动埋点，
        // 因此不需要在Activity中调用MobclickAgent.onResume和onPause方法
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        UMConfigure.getOaid(this) {
            log("OAID $it")
        }

        getTestDeviceInfo(this).run {
            val jsonObject = JSONObject()
            jsonObject["device_id"] = this?.get(0)
            jsonObject["mac"] = this?.get(1)
            log("DeviceInfo ${jsonObject.toJSONString()}")
        }

    }


    private fun getTestDeviceInfo(context: Context?): Array<String?>? {
        val deviceInfo = arrayOfNulls<String>(2)
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context)
                deviceInfo[1] = DeviceConfig.getMac(context)
            }
        } catch (e: java.lang.Exception) {
        }
        return deviceInfo
    }

    private fun initRefreshLayout() {

        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableLoadMore(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)
            MaterialHeader(context).apply {
                setColorSchemeResources(R.color.blue, R.color.blue, R.color.blue)
            }
//            MyRefreshHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableFooterTranslationContent(true)
            layout.setFooterHeight(153f)
            layout.setFooterTriggerRate(0.5f)

//            ClassicsFooter(this).apply {
//                setAccentColorId(R.color.colorTextPrimary)
//            }
            LoadMoreFooter(context)
        }
    }

}