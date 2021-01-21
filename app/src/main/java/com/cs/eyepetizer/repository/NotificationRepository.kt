package com.cs.eyepetizer.repository

import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.bean.PushMessage

/**
 *
 * @author  ChenSen
 * @date  2021/1/15
 * @desc
 **/
class NotificationRepository private constructor(private val api: Api) {


    suspend fun refreshPushMessage(url: String): PushMessage {
        val pushMessage = api.getPushMessage(url)
        //todo 缓存

        return pushMessage
    }


    companion object {
        private var INSTANCE: NotificationRepository? = null

        fun getInstance(api: Api): NotificationRepository {

            return INSTANCE ?: synchronized(this) {
                NotificationRepository(api).apply {
                    INSTANCE = this
                }
            }
        }
    }

}