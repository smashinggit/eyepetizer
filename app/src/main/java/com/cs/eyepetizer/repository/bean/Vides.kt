package com.cs.eyepetizer.repository.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * 视频详情-相关推荐+评论，响应实体类。
 */
data class VideoDetail(
    val videoBeanForClient: VideoBeanForClient?,
    val videoRelated: VideoRelated?,
    val videoReplies: VideoReplies
)


@Parcelize
data class VideoInfo(
    val videoId: Long,
    val playUrl: String,
    val title: String,
    val description: String,
    val category: String,
    val library: String,
    val consumption: Consumption,
    val cover: Cover,
    val author: Author?,
    val webUrl: WebUrl
) : Parcelable






