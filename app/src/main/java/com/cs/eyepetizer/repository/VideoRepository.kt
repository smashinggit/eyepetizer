package com.cs.eyepetizer.repository

import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.network.api.VideoApi
import com.cs.eyepetizer.repository.bean.VideoBeanForClient
import com.cs.eyepetizer.repository.bean.VideoRelated
import com.cs.eyepetizer.repository.bean.VideoReplies
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 **/
class VideoRepository private constructor(
    private val api: VideoApi = RetrofitClient.create(VideoApi::class.java)
) {

    /**
     * 视频信息
     */
    suspend fun refreshVideoBean(videoId: Long): VideoBeanForClient {
        return api.getVideoBeanForClient(videoId)
    }


    /**
     * 刷新视频推荐
     */
    suspend fun refreshVideoRelated(videoId: Long): VideoRelated {
        return api.getVideoRelated(videoId)
    }

    /**
     * 获取视频评论
     */
    suspend fun refreshVideoReplies(repliesUrl: String): VideoReplies {
        return api.getVideoReplies(repliesUrl)
    }


    companion object {

        private var repository: VideoRepository? = null

        fun getInstance(api: VideoApi): VideoRepository {
            return repository ?: synchronized(this) {
                VideoRepository(api).also {
                    repository = it
                }

            }
        }
    }


}