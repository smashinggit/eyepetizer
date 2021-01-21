package com.cs.eyepetizer.ui.community.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cs.common.utils.*
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.CommunityRecommend
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.common.EmptyViewHolder
import com.cs.eyepetizer.ui.community.commend.CommendAdapter
import com.cs.eyepetizer.ui.community.commend.CommendAdapter.Companion.FOLLOW_CARD_TYPE
import com.cs.eyepetizer.ui.community.commend.CommendAdapter.Companion.STR_COMMUNITY_COLUMNS_CARD
import com.cs.eyepetizer.ui.community.commend.CommendAdapter.Companion.STR_FOLLOW_CARD_DATA_TYPE
import com.cs.eyepetizer.utils.showShareDialog
import com.github.chrisbanes.photoview.PhotoView
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import de.hdodenhof.circleimageview.CircleImageView

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc
 **/
class CommunityDetailAdapter(val mContext: Context, var mData: List<CommunityRecommend.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        FOLLOW_CARD_TYPE -> FollowCardViewHolder(
            R.layout.item_community_detail.inflate(
                parent
            )
        )
        else -> EmptyViewHolder(View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun getItemCount() = mData.size

    override fun getItemViewType(position: Int): Int {
        val item = mData[position]
        return when (item.type) {
            STR_COMMUNITY_COLUMNS_CARD -> {
                if (item.data.dataType == STR_FOLLOW_CARD_DATA_TYPE)
                    FOLLOW_CARD_TYPE
                else Constant.ItemViewType.UNKNOWN
            }
            else -> Constant.ItemViewType.UNKNOWN
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mData[position]

        when (holder) {
            is FollowCardViewHolder -> {
                holder.run {
                    videoPlayer.gone()
                    viewPagerPhotos.gone()
                    flHeader.visible()
                    llUgcInfo.visible()

                    ivPullDown.setOnClickListener {
                        if (mContext is CommunityDetailActivity) {
                            mContext.finish()
                        }
                    }
                    ivAvatar.load(item.data.content.data.owner.avatar)
                    if (item.data.content.data.owner.expert) ivAvatarStar.visible() else ivAvatarStar.gone()
                    tvNickName.text = item.data.content.data.owner.nickname
                    tvDescription.text = item.data.content.data.description
                    if (item.data.content.data.description.isBlank()) tvDescription.gone() else tvDescription.visible()
                    tvTagName.text = item.data.content.data.tags?.first()?.name
                    if (item.data.content.data.tags.isNullOrEmpty()) tvTagName.gone() else tvTagName.visible()
                    tvCollectionCount.text =
                        item.data.content.data.consumption.collectionCount.toString()
                    tvReplyCount.text = item.data.content.data.consumption.replyCount.toString()
                    setOnClickListener(
                        tvPrivateLetter,
                        tvFollow,
                        ivCollectionCount,
                        tvCollectionCount,
                        ivReply,
                        tvReplyCount,
                        ivFavorites,
                        tvFavorites,
                        ivShare
                    ) {
                        when (this) {
                            tvPrivateLetter, tvFollow, ivCollectionCount, tvCollectionCount, ivFavorites, tvFavorites -> LoginActivity.start(
                                mContext
                            )
                            ivShare -> {
                                showShareDialog(
                                    mContext as AppCompatActivity,
                                    getShareContent(item)
                                )
                            }
                            ivReply, tvReplyCount -> mContext.toast(mContext.getString(R.string.currently_not_supported))
                            else -> {
                            }
                        }
                    }
                    itemView.setOnClickListener { switchHeaderAndUgcInfoVisibility() }
                }
                when (item.data.content.type) {
                    CommendAdapter.STR_VIDEO_TYPE -> {
                        holder.videoPlayer.visible()
                        holder.videoPlayer.run {
                            val data = item.data.content.data
                            val cover = ImageView(mContext)
                            cover.scaleType = ImageView.ScaleType.CENTER_CROP
                            cover.load(data.cover.detail)
                            cover.parent?.run { removeView(cover) }
                            thumbImageView = cover
                            setThumbPlay(true)
                            setIsTouchWiget(false)
                            isLooping = true
                            playTag = TAG
                            playPosition = position
                            setVideoAllCallBack(object : GSYSampleCallBack() {
                                override fun onClickBlank(url: String?, vararg objects: Any?) {
                                    super.onClickBlank(url, *objects)
                                    holder.switchHeaderAndUgcInfoVisibility()
                                }
                            })
                            setUp(data.playUrl, false, null)
                        }
                    }
                    CommendAdapter.STR_UGC_PICTURE_TYPE -> {
                        holder.viewPagerPhotos.visible()
                        item.data.content.data.urls?.run {
                            holder.viewPagerPhotos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            holder.viewPagerPhotos.offscreenPageLimit = 1
                            holder.viewPagerPhotos.adapter =
                                PhotosAdapter(item.data.content.data.urls, holder)
                            if (item.data.content.data.urls.size > 1) {
                                holder.tvPhotoCount.visible()
                                holder.tvPhotoCount.text = String.format(
                                    mContext.getString(R.string.photo_count),
                                    1,
                                    item.data.content.data.urls.size
                                )
                            } else {
                                holder.tvPhotoCount.gone()
                            }
                            holder.viewPagerPhotos.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    holder.tvPhotoCount.text = String.format(
                                        mContext.getString(R.string.photo_count),
                                        position + 1,
                                        item.data.content.data.urls.size
                                    )
                                }
                            })
                        }
                    }
                    else -> {

                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }

    class FollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoPlayer = view.findViewById<GSYVideoPlayer>(R.id.videoPlayer)
        val viewPagerPhotos = view.findViewById<ViewPager2>(R.id.viewPagerPhotos)
        val ivPullDown = view.findViewById<ImageView>(R.id.ivPullDown)
        val tvPhotoCount = view.findViewById<TextView>(R.id.tvPhotoCount)
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val ivAvatarStar = view.findViewById<CircleImageView>(R.id.ivAvatarStar)
        val tvNickName = view.findViewById<TextView>(R.id.tvNickName)
        val tvPrivateLetter = view.findViewById<TextView>(R.id.tvPrivateLetter)
        val tvFollow = view.findViewById<TextView>(R.id.tvFollow)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvTagName = view.findViewById<TextView>(R.id.tvTagName)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivReply = view.findViewById<ImageView>(R.id.ivReply)
        val tvReplyCount = view.findViewById<TextView>(R.id.tvReplyCount)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val flHeader = view.findViewById<FrameLayout>(R.id.flHeader)
        val llUgcInfo = view.findViewById<LinearLayout>(R.id.llUgcInfo)

        fun switchHeaderAndUgcInfoVisibility() {
            if (ivPullDown.visibility == View.VISIBLE) {
                ivPullDown.invisibleWithAlpha()
                llUgcInfo.invisibleWithAlpha()
            } else {
                ivPullDown.visibleWithAlpha()
                llUgcInfo.visibleWithAlpha()
            }
        }
    }

    private fun getShareContent(item: CommunityRecommend.Item): String {
        item.data.content.data.run {
            val linkUrl =
                "https://www.eyepetizer.net/detail.html?vid=${id}&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0&resourceType=${resourceType}"
            return "${owner.nickname} 在${Versions.applicationName(mContext)}发表了作品：\n「${description}」\n${linkUrl}"
        }
    }

    inner class PhotosAdapter(val dataList: List<String>, val ugcHolder: FollowCardViewHolder) :
        RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

        inner class ViewHolder(view: PhotoView) : RecyclerView.ViewHolder(view) {
            val photoView = view
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val photoView = PhotoView(parent.context)
            photoView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return ViewHolder(photoView)
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.photoView.load(dataList[position])
            holder.photoView.setOnClickListener { ugcHolder.switchHeaderAndUgcInfoVisibility() }
        }
    }

    companion object {
        const val TAG = "CommunityDetailAdapter"
        const val PGC_USER_TYPE = "PGC"
        const val NORMAL_USER_TYPE = "NORMAL"
    }

}