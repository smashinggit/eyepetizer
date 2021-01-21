package com.cs.eyepetizer.ui.home.commend

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.*
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.Commend
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.common.*
import com.cs.eyepetizer.ui.home.detail.DetailActivity
import com.cs.eyepetizer.ui.home.daily.DailyAdapter
import com.cs.eyepetizer.utils.ActionUrlUtil
import com.cs.eyepetizer.utils.showShareDialog
import com.scwang.smart.refresh.layout.util.SmartUtil.dp2px
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc
 **/
class CommendAdapter(private val mContext: Context, private var mData: List<Commend.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = mData.size

    override fun getItemViewType(position: Int): Int =
        ItemTypes.getItemViewType(mData[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolders.getViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = mData[position]

        when (holder) {
            is TextCardViewHeader5ViewHolder -> {
                holder.tvTitle5.text = data.data.text
                if (data.data.actionUrl != null) holder.ivInto5.visible() else holder.ivInto5.gone()
                if (data.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener {
                    mContext.startActivity<LoginActivity>()
                }
                setOnClickListener(
                    holder.tvTitle5,
                    holder.ivInto5
                ) { ActionUrlUtil.process(mContext, data.data.actionUrl, data.data.text) }
            }
            is TextCardViewHeader7ViewHolder -> {
                holder.tvTitle7.text = data.data.text
                holder.tvRightText7.text = data.data.rightText
                setOnClickListener(holder.tvRightText7, holder.ivInto7) {
                    ActionUrlUtil.process(
                        mContext,
                        data.data.actionUrl,
                        "${data.data.text},${data.data.rightText}"
                    )
                }
            }
            is TextCardViewHeader8ViewHolder -> {
                holder.tvTitle8.text = data.data.text
                holder.tvRightText8.text = data.data.rightText
                setOnClickListener(holder.tvRightText8, holder.ivInto8) {
                    ActionUrlUtil.process(
                        mContext,
                        data.data.actionUrl,
                        data.data.text
                    )
                }
            }
            is TextCardViewFooter2ViewHolder -> {
                holder.tvFooterRightText2.text = data.data.text
                setOnClickListener(
                    holder.tvFooterRightText2,
                    holder.ivTooterInto2
                ) { ActionUrlUtil.process(mContext, data.data.actionUrl, data.data.text) }
            }
            is TextCardViewFooter3ViewHolder -> {
                holder.tvFooterRightText3.text = data.data.text
                setOnClickListener(
                    holder.tvRefresh,
                    holder.tvFooterRightText3,
                    holder.ivTooterInto3
                ) {
                    if (this == holder.tvRefresh) {
                        mContext.toast("${holder.tvRefresh.text},${mContext.getString(R.string.currently_not_supported)}")
                    } else if (this == holder.tvFooterRightText3 || this == holder.ivTooterInto3) {
                        ActionUrlUtil.process(mContext, data.data.actionUrl, data.data.text)
                    }
                }
            }
            is FollowCardViewHolder -> {
                holder.ivVideo.load(data.data.content.data.cover.feed, 4f)
                holder.ivAvatar.load(data.data.header.icon)
                holder.tvVideoDuration.text =
                    data.data.content.data.duration.conversionVideoDuration()
                holder.tvDescription.text = data.data.header.description
                holder.tvTitle.text = data.data.header.title
                if (data.data.content.data.ad) holder.tvLabel.visible() else holder.tvLabel.gone()
                if (data.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.ivChoiceness.visible() else holder.ivChoiceness.gone()
                holder.ivShare.setOnClickListener {
                    showShareDialog(
                        mContext as AppCompatActivity,
                        "${data.data.content.data.title}：${data.data.content.data.webUrl.raw}"
                    )
                    mContext.toast("${data.data.content.data.title}：${data.data.content.data.webUrl.raw}")
                }
                holder.itemView.setOnClickListener {
                    data.data.content.data.run {
                        if (ad || author == null) {
                            DetailActivity.start(mContext, id)
                        } else {
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
                                    author,
                                    webUrl
                                )
                            )
                        }
                    }
                }
            }
            is BannerViewHolder -> {
                holder.ivPicture.load(data.data.image, 4f)
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        mContext,
                        data.data.actionUrl,
                        data.data.title
                    )
                }
            }
            is Banner3ViewHolder -> {
                holder.ivPicture.load(data.data.image, 4f)
                holder.ivAvatar.load(data.data.header.icon)
                holder.tvTitle.text = data.data.header.title
                holder.tvDescription.text = data.data.header.description
                if (data.data.label?.text.isNullOrEmpty()) holder.tvLabel.invisible() else holder.tvLabel.visible()
                holder.tvLabel.text = data.data.label?.text ?: ""
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        mContext,
                        data.data.actionUrl,
                        data.data.header.title
                    )
                }
            }
            is InformationCardFollowCardViewHolder -> {
                holder.ivCover.load(
                    data.data.backgroundImage,
                    4f,
                    RoundedCornersTransformation.CornerType.TOP
                )
                holder.recyclerView.setHasFixedSize(true)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(InformationCardFollowCardItemDecoration())
                }
                holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.recyclerView.adapter = InformationCardFollowCardAdapter(
                    mContext,
                    data.data.actionUrl,
                    data.data.titleList
                )
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        mContext,
                        data.data.actionUrl
                    )
                }
            }
            is VideoSmallCardViewHolder -> {
                holder.ivPicture.load(data.data.cover.feed, 4f)
                holder.tvDescription.text =
                    if (data.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${data.data.category} / 开眼精选" else "#${data.data.category}"
                holder.tvTitle.text = data.data.title
                holder.tvVideoDuration.text = data.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener {
                    showShareDialog(
                        mContext as AppCompatActivity,
                        "${data.data.title}：${data.data.webUrl.raw}"
                    )
                    mContext.toast("${data.data.title}：${data.data.webUrl.raw}")
                }
                holder.itemView.setOnClickListener {
                    data.data.run {
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
                                author,
                                webUrl
                            )
                        )
                    }
                }
            }
            is UgcSelectedCardCollectionViewHolder -> {
                holder.tvTitle.text = data.data.header.title
                holder.tvRightText.text = data.data.header.rightText
                holder.tvRightText.setOnClickListener {
                }
                data.data.itemList.forEachIndexed { index, it ->
                    when (index) {
                        0 -> {
                            holder.ivCoverLeft.load(
                                it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.LEFT
                            )
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersLeft.visible()
                            holder.ivAvatarLeft.load(it.data.userCover)
                            holder.tvNicknameLeft.text = it.data.nickname
                        }
                        1 -> {
                            holder.ivCoverRightTop.load(
                                it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.TOP_RIGHT
                            )
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersRightTop.visible()
                            holder.ivAvatarRightTop.load(it.data.userCover)
                            holder.tvNicknameRightTop.text = it.data.nickname
                        }
                        2 -> {
                            holder.ivCoverRightBottom.load(
                                it.data.url,
                                4f,
                                RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
                            )
                            if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) holder.ivLayersRightBottom.visible()
                            holder.ivAvatarRightBottom.load(it.data.userCover)
                            holder.tvNicknameRightBottom.text = it.data.nickname
                        }
                    }
                }
                holder.itemView.setOnClickListener {
                    mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                }
            }
            is TagBriefCardViewHolder -> {
                holder.ivPicture.load(data.data.icon, 4f)
                holder.tvDescription.text = data.data.description
                holder.tvTitle.text = data.data.title
                if (data.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener {
                    mContext.startActivity<LoginActivity>()
                }
                holder.itemView.setOnClickListener {
                    mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                }
            }
            is TopicBriefCardViewHolder -> {
                holder.ivPicture.load(data.data.icon, 4f)
                holder.tvDescription.text = data.data.description
                holder.tvTitle.text = data.data.title
                holder.itemView.setOnClickListener {
                    mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                }
            }
            is AutoPlayVideoAdViewHolder -> {
                data.data.detail?.run {
                    holder.ivAvatar.load(data.data.detail!!.icon)
                    holder.tvTitle.text = data.data.detail!!.title
                    holder.tvDescription.text = data.data.detail!!.description
                    startAutoPlay(
                        mContext,
                        holder.videoPlayer,
                        position,
                        url,
                        imageUrl,
                        TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                ActionUrlUtil.process(mContext, data.data.detail!!.actionUrl)
                            }
                        })
                    setOnClickListener(holder.videoPlayer.thumbImageView, holder.itemView) {
                        ActionUrlUtil.process(mContext, data.data.detail!!.actionUrl)
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }


    class InformationCardFollowCardItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount //item count
            count?.run {
                when (position) {
                    0 -> {
                        outRect.top = dp2px(18f)
                    }
                    count.minus(1) -> {
                        outRect.top = dp2px(13f)
                        outRect.bottom = dp2px(18f)
                    }
                    else -> {
                        outRect.top = dp2px(13f)
                    }
                }
            }
        }
    }

    class InformationCardFollowCardAdapter(
        val activity: Context,
        val actionUrl: String?,
        val dataList: List<String>
    ) :
        RecyclerView.Adapter<InformationCardFollowCardAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNews = view.findViewById<TextView>(R.id.tvNews)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): InformationCardFollowCardAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_information_card_type_item, parent, false)
            )
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(
            holder: InformationCardFollowCardAdapter.ViewHolder,
            position: Int
        ) {
            val item = dataList[position]
            holder.tvNews.text = item
            holder.itemView.setOnClickListener { ActionUrlUtil.process(activity, actionUrl) }
        }
    }

    companion object {
        const val TAG = "CommendAdapter"
        fun startAutoPlay(
            activity: Context,
            player: GSYVideoPlayer,
            position: Int,
            playUrl: String,
            coverUrl: String,
            playTag: String,
            callBack: GSYSampleCallBack? = null
        ) {
            player.run {
                //防止错位设置
                setPlayTag(playTag)
                //设置播放位置防止错位
                playPosition = position
                //音频焦点冲突时是否释放
                isReleaseWhenLossAudio = false
                //设置循环播放
                isLooping = true
                //增加封面
                val cover = ImageView(activity)
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                cover.load(coverUrl, 4f)
                cover.parent?.run { removeView(cover) }
                thumbImageView = cover
                //设置播放过程中的回调
                setVideoAllCallBack(callBack)
                //设置播放URL
                setUp(playUrl, false, null)
            }
        }
    }

}