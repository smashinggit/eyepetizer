package com.cs.eyepetizer.ui.home.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.*
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.repository.bean.VideoRelated
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.common.ItemTypes
import com.cs.eyepetizer.ui.common.TextCardViewHeader4ViewHolder
import com.cs.eyepetizer.ui.common.VideoSmallCardViewHolder
import com.cs.eyepetizer.ui.common.ViewHolders
import com.cs.eyepetizer.ui.home.daily.DailyAdapter
import com.cs.eyepetizer.utils.showShareDialog

/**
 *
 * @author  ChenSen
 * @date  2021/1/19
 * @desc
 **/
class DetailRelatedAdapter(
    private val mContext: Context, private val mData: List<VideoRelated.Item>,
    private var videoInfoData: VideoInfo?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Constant.ItemViewType.CUSTOM_HEADER -> CustomHeaderViewHolder(
                R.layout.item_new_detail_custom_header_type.inflate(parent)
            )
            SIMPLE_HOT_REPLY_CARD_TYPE -> SimpleHotReplyCardViewHolder(View(parent.context))
            else -> ViewHolders.getViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int = mData.size
    override fun getItemViewType(position: Int) = when {
        position == 0 -> Constant.ItemViewType.CUSTOM_HEADER
        mData[position - 1].type == "simpleHotReplyScrollCard" && mData[position - 1].data.dataType == "ItemCollection" -> SIMPLE_HOT_REPLY_CARD_TYPE
        else -> ItemTypes.getItemViewType(mData[position - 1])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomHeaderViewHolder -> {
                videoInfoData?.let {
                    holder.run {
                        groupAuthor.gone()
                        tvTitle.text = videoInfoData?.title
                        tvCategory.text =
                            if (videoInfoData?.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${videoInfoData?.category} / 开眼精选" else "#${videoInfoData?.category}"
                        tvDescription.text = videoInfoData?.description
                        tvCollectionCount.text =
                            videoInfoData?.consumption?.collectionCount.toString()
                        tvShareCount.text = videoInfoData?.consumption?.shareCount.toString()
                        videoInfoData?.author?.run {
                            groupAuthor.visible()
                            ivAvatar.load(videoInfoData?.author?.icon ?: "")
                            tvAuthorDescription.text = videoInfoData?.author?.description
                            tvAuthorName.text = videoInfoData?.author?.name
                        }
                        setOnClickListener(
                            ivCollectionCount,
                            tvCollectionCount,
                            ivShare,
                            tvShareCount,
                            ivCache,
                            tvCache,
                            ivFavorites,
                            tvFavorites,
                            tvFollow
                        ) {
                            when (this) {
                                ivCollectionCount, tvCollectionCount, ivFavorites, tvFavorites -> LoginActivity.start(
                                    mContext
                                )
                                ivShare, tvShareCount -> showShareDialog(
                                    mContext as AppCompatActivity,
                                    "${videoInfoData?.title}：${videoInfoData?.webUrl?.raw}"
                                )
                                ivCache, tvCache -> mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                                tvFollow -> LoginActivity.start(mContext)
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
            is SimpleHotReplyCardViewHolder -> {
                // 不做任何处理，忽略此ViewHolder。
            }
            is TextCardViewHeader4ViewHolder -> {
                val item = mData[position - 1]
                holder.tvTitle4.text = item.data.text
            }
            is VideoSmallCardViewHolder -> {
                val item = mData[position - 1]
                holder.ivPicture.load(item.data.cover.feed, 4f)
                holder.tvDescription.text =
                    if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.tvDescription.setTextColor(
                    ContextCompat.getColor(mContext, R.color.whiteAlpha35)
                )
                holder.tvTitle.text = item.data.title
                holder.tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                holder.tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener {
                    showShareDialog(
                        mContext as AppCompatActivity,
                        "${item.data.title}：${item.data.webUrl.raw}"
                    )
                }
                holder.itemView.setOnClickListener {
                    item.data.run {
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
                        (mContext as DetailActivity).scrollTop()
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }


    fun bindVideoInfo(videoInfoData: VideoInfo?) {
        this.videoInfoData = videoInfoData
    }

    inner class CustomHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvCategory = view.findViewById<TextView>(R.id.tvCategory)
        val ivFoldText = view.findViewById<ImageView>(R.id.ivFoldText)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val tvShareCount = view.findViewById<TextView>(R.id.tvShareCount)
        val ivCache = view.findViewById<ImageView>(R.id.ivCache)
        val tvCache = view.findViewById<TextView>(R.id.tvCache)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvAuthorDescription = view.findViewById<TextView>(R.id.tvAuthorDescription)
        val tvAuthorName = view.findViewById<TextView>(R.id.tvAuthorName)
        val tvFollow = view.findViewById<TextView>(R.id.tvFollow)
        val groupAuthor = view.findViewById<Group>(R.id.groupAuthor)
    }

    /**
     * 相关推荐，数据集合里附带的热门评论，UI展示上不做处理。
     */
    inner class SimpleHotReplyCardViewHolder(view: View) : RecyclerView.ViewHolder(view)


    companion object {
        const val TAG = "NewDetailRelatedAdapter"
        const val SIMPLE_HOT_REPLY_CARD_TYPE = Constant.ItemViewType.MAX
    }


}