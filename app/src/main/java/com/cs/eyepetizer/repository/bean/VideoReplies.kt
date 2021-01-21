package com.cs.eyepetizer.repository.bean

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc 视频详情-评论列表。响应实体类。
 **/
data class VideoReplies(
    val itemList: List<Item>,
    val count: Int,
    val total: Int,
    val nextPageUrl: String?,
    val adExist: Boolean
) : Model() {

    data class Item(
        val `data`: Data,
        val type: String,
        val tag: Any?,
        val id: Int = 0,
        val adIndex: Int
    )

    data class Data(
        val actionUrl: String?,
        val adTrack: Any,
        val cover: Any,
        val createTime: Long,
        val dataType: String,
        val follow: Author.Follow?,
        val hot: Boolean,
        val id: Long,
        val imageUrl: Any,
        val likeCount: Int,
        val liked: Boolean,
        val message: String,
        val parentReply: ParentReply?,
        val parentReplyId: Long,
        val replyStatus: String,
        val rootReplyId: Long,
        val sequence: Int,
        val showConversationButton: Boolean,
        val showParentReply: Boolean,
        val sid: String,
        val subTitle: Any,
        val text: String,
        val type: String,
        val ugcVideoId: Any,
        val ugcVideoUrl: Any,
        val user: User?,
        val userBlocked: Boolean,
        val userType: Any,
        val videoId: Long,
        val videoTitle: String
    )

    data class ParentReply(
        val id: Long,
        val imageUrl: Any,
        val message: String,
        val replyStatus: String,
        val user: User?
    )

    data class User(
        val actionUrl: String,
        val area: Any,
        val avatar: String?,
        val birthday: Long,
        val city: Any,
        val country: Any,
        val cover: String,
        val description: String,
        val expert: Boolean,
        val followed: Boolean,
        val gender: String,
        val ifPgc: Boolean,
        val job: Any,
        val library: String,
        val limitVideoOpen: Boolean,
        val nickname: String,
        val registDate: Long,
        val releaseDate: Long,
        val uid: Int,
        val university: Any,
        val userType: String
    )
}

