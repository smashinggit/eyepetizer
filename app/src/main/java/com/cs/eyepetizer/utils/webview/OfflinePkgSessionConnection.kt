package com.cs.eyepetizer.utils.webview

import android.content.Context
import android.content.Intent
import com.tencent.sonic.sdk.SonicConstants
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConnection
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 **/

class OfflinePkgSessionConnection(context: Context, session: SonicSession?, intent: Intent?) :
    SonicSessionConnection(session, intent) {

    private val mContext: WeakReference<Context> = WeakReference(context)

    override fun internalGetCustomHeadFieldEtag(): String {
        return CUSTOM_HEAD_FILED_ETAG
    }

    override fun internalConnect(): Int {
        val ctx = mContext.get()
        if (null != ctx) {
            try {
                val offlineHtmlInputStream = ctx.assets.open("sonic-demo-index.html")
                responseStream = BufferedInputStream(offlineHtmlInputStream)
                return SonicConstants.ERROR_CODE_SUCCESS
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return SonicConstants.ERROR_CODE_UNKNOWN
    }

    override fun internalGetResponseStream(): BufferedInputStream {
        return responseStream
    }

    override fun disconnect() {
        if (null != responseStream) {
            try {
                responseStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getResponseHeaderFields(): MutableMap<String, MutableList<String>> {
        return HashMap(0)
    }

    override fun getResponseCode(): Int {
        return 200
    }

    override fun getResponseHeaderField(key: String?): String {
        return ""
    }

}