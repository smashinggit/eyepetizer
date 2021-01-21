package com.cs.eyepetizer.ui.common

import com.cs.common.utils.Logs
import com.cs.eyepetizer.Constant.ItemViewType.AUTO_PLAY_VIDEO_AD
import com.cs.eyepetizer.Constant.ItemViewType.BANNER
import com.cs.eyepetizer.Constant.ItemViewType.BANNER3
import com.cs.eyepetizer.Constant.ItemViewType.COLUMN_CARD_LIST
import com.cs.eyepetizer.Constant.ItemViewType.FOLLOW_CARD
import com.cs.eyepetizer.Constant.ItemViewType.HORIZONTAL_SCROLL_CARD
import com.cs.eyepetizer.Constant.ItemViewType.INFORMATION_CARD
import com.cs.eyepetizer.Constant.ItemViewType.SPECIAL_SQUARE_CARD_COLLECTION
import com.cs.eyepetizer.Constant.ItemViewType.TAG_BRIEFCARD
import com.cs.eyepetizer.Constant.ItemViewType.TEXT_CARD_FOOTER2
import com.cs.eyepetizer.Constant.ItemViewType.TEXT_CARD_FOOTER3
import com.cs.eyepetizer.Constant.ItemViewType.TEXT_CARD_HEADER4
import com.cs.eyepetizer.Constant.ItemViewType.TEXT_CARD_HEADER5
import com.cs.eyepetizer.Constant.ItemViewType.TEXT_CARD_HEADER7
import com.cs.eyepetizer.Constant.ItemViewType.TEXT_CARD_HEADER8
import com.cs.eyepetizer.Constant.ItemViewType.TOPIC_BRIEFCARD
import com.cs.eyepetizer.Constant.ItemViewType.UGC_SELECTED_CARD_COLLECTION
import com.cs.eyepetizer.Constant.ItemViewType.UNKNOWN
import com.cs.eyepetizer.Constant.ItemViewType.VIDEO_SMALL_CARD
import com.cs.eyepetizer.repository.bean.*
import kotlin.math.log


/**
 *
 * @author  ChenSen
 * @date  2021/1/12
 * @desc
 **/
object ItemTypes {

    fun getItemViewType(item: Discovery.Item): Int {
        return if (item.type == "textCard")
            getTextCardType(item.data.type)
        else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: Commend.Item): Int {
        return if (item.type == "textCard")
            getTextCardType(item.data.type)
        else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: Daily.Item): Int {
        return if (item.type == "textCard") getTextCardType(
            item.data.type
        ) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: Follow.Item): Int {
        return if (item.type == "textCard") getTextCardType(
            item.data.type
        ) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: VideoRelated.Item): Int {
        return if (item.type == "textCard") getTextCardType(
            item.data.type
        ) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: VideoReplies.Item): Int {
        return if (item.type == "textCard") getTextCardType(
            item.data.type
        ) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }


    private fun getItemViewType(type: String, dataType: String): Int {
        return when (type) {

            "horizontalScrollCard" -> {
                when (dataType) {
                    "HorizontalScrollCard" -> HORIZONTAL_SCROLL_CARD
                    else -> UNKNOWN
                }
            }
            "specialSquareCardCollection" -> {
                when (dataType) {
                    "ItemCollection" -> SPECIAL_SQUARE_CARD_COLLECTION
                    else -> UNKNOWN
                }
            }
            "columnCardList" -> {
                when (dataType) {
                    "ItemCollection" -> COLUMN_CARD_LIST
                    else -> UNKNOWN
                }
            }
            /*"textCard" -> {
                when (item.data.type) {
                    "header5" -> TEXT_CARD_HEADER5
                    "header7" -> TEXT_CARD_HEADER7
                    "header8" -> TEXT_CARD_HEADER8
                    "footer2" -> TEXT_CARD_FOOTER2
                    "footer3" -> TEXT_CARD_FOOTER3
                    else -> UNKNOWN
                }
            }*/
            "banner" -> {
                when (dataType) {
                    "Banner" -> BANNER
                    else -> UNKNOWN
                }
            }
            "banner3" -> {
                when (dataType) {
                    "Banner" -> BANNER3
                    else -> UNKNOWN
                }
            }
            "videoSmallCard" -> {
                when (dataType) {
                    "VideoBeanForClient" -> VIDEO_SMALL_CARD
                    else -> UNKNOWN
                }
            }
            "briefCard" -> {
                when (dataType) {
                    "TagBriefCard" -> TAG_BRIEFCARD
                    "TopicBriefCard" -> TOPIC_BRIEFCARD
                    else -> UNKNOWN
                }
            }
            "followCard" -> {
                when (dataType) {
                    "FollowCard" -> FOLLOW_CARD
                    else -> UNKNOWN
                }
            }
            "informationCard" -> {
                when (dataType) {
                    "InformationCard" -> INFORMATION_CARD
                    else -> UNKNOWN
                }
            }
            "ugcSelectedCardCollection" -> {
                when (dataType) {
                    "ItemCollection" -> UGC_SELECTED_CARD_COLLECTION
                    else -> UNKNOWN
                }
            }
            "autoPlayVideoAd" -> {
                when (dataType) {
                    "AutoPlayVideoAdDetail" -> AUTO_PLAY_VIDEO_AD
                    else -> UNKNOWN
                }
            }
            else -> UNKNOWN
        }
    }


    private fun getTextCardType(type: String) = when (type) {
        "header4" -> TEXT_CARD_HEADER4
        "header5" -> TEXT_CARD_HEADER5
        "header7" -> TEXT_CARD_HEADER7
        "header8" -> TEXT_CARD_HEADER8
        "footer2" -> TEXT_CARD_FOOTER2
        "footer3" -> TEXT_CARD_FOOTER3
        else -> UNKNOWN
    }
}