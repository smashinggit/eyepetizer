package com.cs.eyepetizer.network.api

import com.alibaba.fastjson.JSONObject
import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.repository.bean.*
import retrofit2.http.GET
import retrofit2.http.Url

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc
 **/
interface Api {

    //首页-发现列表
    @GET
    suspend fun getDiscovery(@Url url: String): Discovery

    //首页-推荐列表
    @GET
    suspend fun getHomePageRecommend(@Url url: String): Commend

    //首页-日报列表
    @GET
    suspend fun getDaily(@Url url: String): Daily

    //社区-推荐列表
    @GET
    suspend fun getCommunityRecommend(@Url url: String): CommunityRecommend

    //社区-关注列表
    @GET
    suspend fun getCommunityFollow(@Url url: String): Follow

    //通知-推送列表
    @GET
    suspend fun getPushMessage(@Url url: String): PushMessage

    //搜索-热搜关键词
    @GET("api/v3/queries/hot")
    suspend fun getHotSearch(): JSONObject


    companion object {

        //首页-发现列表
        const val DISCOVERY_URL = "${RetrofitClient.BASE_URL}api/v7/index/tab/discovery"

        // 首页-推荐列表
        const val HOMEPAGE_RECOMMEND_URL =
            "${RetrofitClient.BASE_URL}api/v5/index/tab/allRec?page=0"

        //首页-日报列表
        const val DAILY_URL = "${RetrofitClient.BASE_URL}api/v5/index/tab/feed"

        //社区-推荐列表
        const val COMMUNITY_RECOMMEND_URL = "${RetrofitClient.BASE_URL}api/v7/community/tab/rec"

        //社区-关注列表
        const val FOLLOW_URL = "${RetrofitClient.BASE_URL}api/v6/community/tab/follow"

        // 通知-推送列表
        const val PUSHMESSAGE_URL = "${RetrofitClient.BASE_URL}api/v3/messages"
    }


}