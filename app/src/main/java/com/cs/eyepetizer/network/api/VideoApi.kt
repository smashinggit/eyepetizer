package com.cs.eyepetizer.network.api

import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.repository.bean.VideoBeanForClient
import com.cs.eyepetizer.repository.bean.VideoRelated
import com.cs.eyepetizer.repository.bean.VideoReplies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 **/
interface VideoApi {

    /**
     * 视频详情-视频信息
     */
    @GET("api/v2/video/{id}")
    suspend fun getVideoBeanForClient(@Path("id") videoId: Long): VideoBeanForClient

    /**
     * 视频详情-推荐列表
     */
    @GET("api/v4/video/related")
    suspend fun getVideoRelated(@Query("id") videoId: Long): VideoRelated

    /**
     * 视频详情-评论列表
     */
    @GET
    suspend fun getVideoReplies(@Url url: String): VideoReplies


    companion object {

        //视频详情-评论列表URL
        const val VIDEO_REPLIES_URL = "${RetrofitClient.BASE_URL}api/v2/replies/video?videoId="
    }

}