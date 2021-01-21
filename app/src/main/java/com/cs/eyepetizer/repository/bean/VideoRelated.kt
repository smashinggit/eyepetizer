package com.cs.eyepetizer.repository.bean

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc 视频详情-相关推荐，响应实体类
 **/
data class VideoRelated(
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
        val actionUrl: String,
        val ad: Boolean,
        val adTrack: Any,
        val author: Author,
        val brandWebsiteInfo: Any,
        val campaign: Any,
        val category: String,
        val collected: Boolean,
        val consumption: Consumption,
        val count: Int,
        val cover: Cover,
        val dataType: String,
        val date: Long,
        val description: String,
        val descriptionEditor: String,
        val descriptionPgc: String,
        val duration: Int,
        val favoriteAdTrack: Any,
        val follow: Author.Follow?,
        val footer: Any,
        val header: Any,
        val id: Long,
        val idx: Int,
        val ifLimitVideo: Boolean,
        val itemList: List<ItemX>,
        val label: Any,
        val labelList: List<Any>,
        val lastViewTime: Any,
        val library: String,
        val playInfo: List<PlayInfo>,
        val playUrl: String,
        val played: Boolean,
        val playlists: Any,
        val promotion: Any,
        val provider: Provider,
        val reallyCollected: Boolean,
        val recallSource: String,
        val releaseTime: Long,
        val remark: String,
        val resourceType: String,
        val searchWeight: Int,
        val shareAdTrack: Any,
        val slogan: Any,
        val src: Int,
        val subTitle: Any,
        val subtitles: List<Any>,
        val tags: List<Tag>,
        val text: String,
        val thumbPlayUrl: Any,
        val title: String,
        val titlePgc: String,
        val type: String,
        val waterMarks: Any,
        val webAdTrack: Any,
        val webUrl: WebUrl
    )

    data class ItemX(
        val adIndex: Int,
        val `data`: DataX,
        val id: Int,
        val tag: Any,
        val type: String
    )

    data class DataX(
        val actionUrl: Any,
        val contentType: String,
        val dataType: String,
        val message: String,
        val nickname: String
    )
}