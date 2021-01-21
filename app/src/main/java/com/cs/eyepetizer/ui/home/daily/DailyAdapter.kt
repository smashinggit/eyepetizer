package com.cs.eyepetizer.ui.home.daily

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.*
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.Daily
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.common.*
import com.cs.eyepetizer.ui.home.detail.DetailActivity
import com.cs.eyepetizer.ui.home.commend.CommendAdapter
import com.cs.eyepetizer.utils.ActionUrlUtil
import com.cs.eyepetizer.utils.showShareDialog
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *
 * @author  Administrator
 * @date  2021/1/14
 * @desc
 **/
class DailyAdapter(private val mContext: Context, private val mData: List<Daily.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return ItemTypes.getItemViewType(mData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolders.getViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mData[position]
        when (holder) {
            is TextCardViewHeader5ViewHolder -> {
                holder.tvTitle5.text = item.data.text
                if (item.data.actionUrl != null) holder.ivInto5.visible() else holder.ivInto5.gone()
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener {
                    mContext.startActivity<LoginActivity>()
                }
                setOnClickListener(
                    holder.tvTitle5,
                    holder.ivInto5
                ) { ActionUrlUtil.process(mContext, item.data.actionUrl, item.data.text) }
            }
            is TextCardViewHeader7ViewHolder -> {
                holder.tvTitle7.text = item.data.text
                holder.tvRightText7.text = item.data.rightText
                setOnClickListener(holder.tvRightText7, holder.ivInto7) {
                    ActionUrlUtil.process(
                        mContext,
                        item.data.actionUrl,
                        "${item.data.text},${item.data.rightText}"
                    )
                }
            }
            is TextCardViewHeader8ViewHolder -> {
                holder.tvTitle8.text = item.data.text
                holder.tvRightText8.text = item.data.rightText
                setOnClickListener(holder.tvRightText8, holder.ivInto8) {
                    ActionUrlUtil.process(
                        mContext,
                        item.data.actionUrl,
                        item.data.text
                    )
                }
            }
            is TextCardViewFooter2ViewHolder -> {
                holder.tvFooterRightText2.text = item.data.text
                setOnClickListener(
                    holder.tvFooterRightText2,
                    holder.ivTooterInto2
                ) { ActionUrlUtil.process(mContext, item.data.actionUrl, item.data.text) }
            }
            is TextCardViewFooter3ViewHolder -> {
                holder.tvFooterRightText3.text = item.data.text
                setOnClickListener(
                    holder.tvRefresh,
                    holder.tvFooterRightText3,
                    holder.ivTooterInto3
                ) {
                    if (this == holder.tvRefresh) {
                        mContext.toast("${holder.tvRefresh.text},${mContext.getString(R.string.currently_not_supported)}")
                    } else if (this == holder.tvFooterRightText3 || this == holder.ivTooterInto3) {
                        ActionUrlUtil.process(mContext, item.data.actionUrl, item.data.text)
                    }
                }
            }
            is Banner3ViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvTitle.text = item.data.header.title
                holder.tvDescription.text = item.data.header.description
                if (item.data.label?.text.isNullOrEmpty()) holder.tvLabel.invisible() else holder.tvLabel.visible()
                holder.tvLabel.text = item.data.label?.text ?: ""
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        mContext,
                        item.data.actionUrl,
                        item.data.header.title
                    )
                }
            }
            is FollowCardViewHolder -> {
                holder.ivVideo.load(item.data.content.data.cover.feed, 4f)
                holder.ivAvatar.load(item.data.header.icon)
                holder.tvVideoDuration.text =
                    item.data.content.data.duration.conversionVideoDuration()
                holder.tvDescription.text = item.data.header.description
                holder.tvTitle.text = item.data.header.title
                if (item.data.content.data.ad) holder.tvLabel.visible() else holder.tvLabel.gone()
                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.ivChoiceness.visible() else holder.ivChoiceness.gone()
                holder.ivShare.setOnClickListener {
                    showShareDialog(
                        mContext as AppCompatActivity,
                        "${item.data.content.data.title}：${item.data.content.data.webUrl.raw}"
                    )
                }
                holder.itemView.setOnClickListener {
                    item.data.content.data.run {
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
            is InformationCardFollowCardViewHolder -> {
                holder.ivCover.load(
                    item.data.backgroundImage,
                    4f,
                    RoundedCornersTransformation.CornerType.TOP
                )
                holder.recyclerView.setHasFixedSize(true)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(CommendAdapter.InformationCardFollowCardItemDecoration())
                }
                holder.recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.recyclerView.adapter = CommendAdapter.InformationCardFollowCardAdapter(
                    mContext,
                    item.data.actionUrl,
                    item.data.titleList
                )
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        mContext,
                        item.data.actionUrl
                    )
                }
            }
            else -> {
                holder.itemView.gone()
                mContext.log("gone")
            }
        }

    }

    companion object {
        const val TAG = "DailyAdapter"
        const val DEFAULT_LIBRARY_TYPE = "DEFAULT"
        const val NONE_LIBRARY_TYPE = "NONE"
        const val DAILY_LIBRARY_TYPE = "DAILY"
    }

}