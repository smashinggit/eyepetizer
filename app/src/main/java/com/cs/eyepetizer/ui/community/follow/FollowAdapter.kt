package com.cs.eyepetizer.ui.community.follow

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.*
import com.cs.common.utils.Dates.getDate
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.Follow
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.common.EmptyViewHolder
import com.cs.eyepetizer.ui.home.commend.CommendAdapter
import com.cs.eyepetizer.ui.home.detail.DetailActivity
import com.cs.eyepetizer.utils.showShareDialog
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc
 **/
class FollowAdapter(val mContext: Context, var mData: List<Follow.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = mData.size + 1

    override fun getItemViewType(position: Int) = when {
        position == 0 -> Constant.ItemViewType.CUSTOM_HEADER
        mData[position - 1].type == "autoPlayFollowCard" && mData[position - 1].data.dataType == "FollowCard" -> AUTO_PLAY_FOLLOW_CARD
        else -> Constant.ItemViewType.UNKNOWN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            Constant.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(
                R.layout.item_community_follow_header_type.inflate(
                    parent
                )
            )
            AUTO_PLAY_FOLLOW_CARD -> AutoPlayFollowCardViewHolder(
                R.layout.item_community_auto_play_follow_card_follow_card_type.inflate(
                    parent
                )
            )
            else -> EmptyViewHolder(View(parent.context))
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.itemView.setOnClickListener { LoginActivity.start(mContext) }

            is AutoPlayFollowCardViewHolder -> {
                val item = mData[position - 1]
                item.data.content.data.run {
                    holder.ivAvatar.load(item.data.header.icon ?: author?.icon ?: "")

                    holder.tvReleaseTime.text = getDate(
                        releaseTime ?: author?.latestReleaseTime ?: System.currentTimeMillis(),
                        "HH:mm"
                    )
                    holder.tvTitle.text = title
                    holder.tvNickname.text = author?.name ?: ""
                    holder.tvContent.text = description
                    holder.tvCollectionCount.text = consumption.collectionCount.toString()
                    holder.tvReplyCount.text = consumption.replyCount.toString()
                    holder.tvVideoDuration.visible()    //视频播放后，复用tvVideoDuration直接隐藏了
                    holder.tvVideoDuration.text = duration.conversionVideoDuration()
                    CommendAdapter.startAutoPlay(
                        mContext,
                        holder.videoPlayer,
                        position,
                        playUrl,
                        cover.feed,
                        TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                holder.tvVideoDuration.gone()
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickResume(url: String?, vararg objects: Any?) {
                                super.onClickResume(url, *objects)
                                holder.tvVideoDuration.gone()
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                holder.tvVideoDuration.visible()
                                DetailActivity.start(
                                    mContext,
                                    VideoInfo(
                                        id,
                                        playUrl,
                                        title,
                                        description,
                                        category,
                                        library,
                                        consumption,
                                        cover,
                                        author!!,
                                        webUrl
                                    )
                                )
                            }
                        })
                    holder.let {
                        setOnClickListener(
                            it.videoPlayer.thumbImageView,
                            it.itemView,
                            it.ivCollectionCount,
                            it.tvCollectionCount,
                            it.ivFavorites,
                            it.tvFavorites,
                            it.ivShare
                        )
                        {
                            when (this) {
                                it.videoPlayer.thumbImageView, it.itemView -> {
                                    DetailActivity.start(
                                        mContext, VideoInfo(
                                            item.data.content.data.id,
                                            playUrl,
                                            title,
                                            description,
                                            category,
                                            library,
                                            consumption,
                                            cover,
                                            author!!,
                                            webUrl
                                        )
                                    )
                                }
                                it.ivCollectionCount, it.tvCollectionCount, it.ivFavorites, it.tvFavorites -> {
                                    LoginActivity.start(mContext)
                                }
                                it.ivShare -> {
                                    showShareDialog(
                                        mContext as AppCompatActivity,
                                        getShareContent(item)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }

    private fun getShareContent(item: Follow.Item): String {
        item.data.content.data.run {
            val linkUrl =
                "${item.data.content.data.webUrl.raw}&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0&resourceType=${resourceType}"
            return "${title}|${Versions.appVersionName(mContext)}：\n${linkUrl}"
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class AutoPlayFollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvReleaseTime = view.findViewById<TextView>(R.id.tvReleaseTime)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvNickname = view.findViewById<TextView>(R.id.tvNickname)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivReply = view.findViewById<ImageView>(R.id.ivReply)
        val tvReplyCount = view.findViewById<TextView>(R.id.tvReplyCount)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val tvVideoDuration = view.findViewById<TextView>(R.id.tvVideoDuration)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val videoPlayer: GSYVideoPlayer = view.findViewById<GSYVideoPlayer>(R.id.videoPlayer)
    }

    companion object {
        const val TAG = "FollowAdapter"
        const val AUTO_PLAY_FOLLOW_CARD = Constant.ItemViewType.MAX
    }


}