package com.cs.eyepetizer.ui.home.discover

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.*
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.Discovery
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.common.*
import com.cs.eyepetizer.ui.home.detail.DetailActivity
import com.cs.eyepetizer.ui.home.commend.CommendAdapter
import com.cs.eyepetizer.ui.home.daily.DailyAdapter
import com.cs.eyepetizer.utils.ActionUrlUtil
import com.cs.eyepetizer.utils.showShareDialog
import com.cs.eyepetizer.view.GridListItemDecoration
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.youth.banner.adapter.BannerAdapter
import kotlinx.android.synthetic.main.item_banner_item_type.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/13
 * @desc
 **/
class DiscoverAdapter(private val mContext: Context, private val mData: List<Discovery.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = mData.size


    override fun getItemViewType(position: Int): Int {
        return ItemTypes.getItemViewType(mData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolders.getViewHolder(parent, viewType)
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
                        mContext.toast("${item.data.header.rightText},${mContext.getString(R.string.currently_not_supported)}")
                    } else if (this == holder.tvFooterRightText3 || this == holder.ivTooterInto3) {
                        ActionUrlUtil.process(mContext, item.data.actionUrl, item.data.text)
                    }
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

            is HorizontalScrollCardViewHolder -> {
                holder.banner.apply {
                    setAdapter(HorizontalScrollCardAdapter(), false)
                    setDatas(item.data.itemList)
                    setBannerRound(dp2px(4f))

                    setOnBannerListener { _, position ->
                        ActionUrlUtil.process(
                            mContext,
                            item.data.itemList[position].data.actionUrl,
                            item.data.itemList[position].data.title
                        )
                    }
                }
            }
            is SpecialSquareCardCollectionViewHolder -> {
                holder.tvTitle.text = item.data.header.title
                holder.tvRightText.text = item.data.header.rightText
                setOnClickListener(
                    holder.tvRightText,
                    holder.ivInto
                ) {
                    mContext.toast("${item.data.header.rightText},${mContext.getString(R.string.currently_not_supported)}")
                }
                holder.recyclerView.setHasFixedSize(true)
                holder.recyclerView.isNestedScrollingEnabled = true
                holder.recyclerView.layoutManager = GridLayoutManager(mContext, 2).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(SpecialSquareCardCollectionItemDecoration())
                }
                val list =
                    item.data.itemList.filter { it.type == "squareCardOfCategory" && it.data.dataType == "SquareCard" }
                holder.recyclerView.adapter = SpecialSquareCardCollectionAdapter(list)
            }
            is ColumnCardListViewHolder -> {
                holder.tvTitle.text = item.data.header.title
                holder.tvRightText.text = item.data.header.rightText
                setOnClickListener(
                    holder.tvRightText,
                    holder.ivInto
                ) {
                    mContext.toast("${item.data.header.rightText},${mContext.getString(R.string.currently_not_supported)}")
                }
                holder.recyclerView.setHasFixedSize(true)
                holder.recyclerView.layoutManager = GridLayoutManager(mContext, 2)
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(GridListItemDecoration(2))
                }
                val list =
                    item.data.itemList.filter { it.type == "squareCardOfColumn" && it.data.dataType == "SquareCard" }
                holder.recyclerView.adapter = ColumnCardListAdapter(list)
            }
            is BannerViewHolder -> {
                holder.ivPicture.load(item.data.image, 4f)
                holder.itemView.setOnClickListener {
                    ActionUrlUtil.process(
                        mContext,
                        item.data.actionUrl,
                        item.data.title
                    )
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
            is VideoSmallCardViewHolder -> {
                holder.ivPicture.load(item.data.cover.feed, 4f)
                holder.tvDescription.text =
                    if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.tvTitle.text = item.data.title
                holder.tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener {
                    showShareDialog(
                        mContext as AppCompatActivity,
                        "${item.data.content.data.title}：${item.data.content.data.webUrl.raw}"
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
                    }
                }
            }
            is TagBriefCardViewHolder -> {
                holder.ivPicture.load(item.data.icon, 4f)
                holder.tvDescription.text = item.data.description
                holder.tvTitle.text = item.data.title
                if (item.data.follow != null) holder.tvFollow.visible() else holder.tvFollow.gone()
                holder.tvFollow.setOnClickListener {
                    mContext.startActivity<LoginActivity>()
                }
                holder.itemView.setOnClickListener {
                    mContext.toast("${item.data.title},${mContext.getString(R.string.currently_not_supported)}")
                }
            }
            is TopicBriefCardViewHolder -> {
                holder.ivPicture.load(item.data.icon, 4f)
                holder.tvDescription.text = item.data.description
                holder.tvTitle.text = item.data.title
                holder.itemView.setOnClickListener {
                    mContext.toast("${item.data.title},${mContext.getString(R.string.currently_not_supported)}")
                }
            }
            is AutoPlayVideoAdViewHolder -> {
                item.data.detail?.run {
                    holder.ivAvatar.load(item.data.detail.icon)
                    holder.tvTitle.text = item.data.detail.title
                    holder.tvDescription.text = item.data.detail.description
                    CommendAdapter.startAutoPlay(
                        mContext,
                        holder.videoPlayer,
                        position,
                        url,
                        imageUrl,
                        CommendAdapter.TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                ActionUrlUtil.process(mContext, item.data.detail.actionUrl)
                            }
                        })
                    setOnClickListener(holder.videoPlayer.thumbImageView, holder.itemView) {
                        ActionUrlUtil.process(mContext, item.data.detail.actionUrl)
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }

    }


    inner class HorizontalScrollCardAdapter :
        BannerAdapter<Discovery.ItemX, HorizontalScrollCardAdapter.BannerViewHolder>(null) {

        override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
            val view =
                LayoutInflater.from(mContext).inflate(R.layout.item_banner_item_type, parent, false)
            return BannerViewHolder(view)
        }

        override fun onBindView(
            holder: BannerViewHolder,
            data: Discovery.ItemX,
            position: Int,
            size: Int
        ) {
            val ivPicture = holder.itemView.ivPicture
            val tvLabel = holder.itemView.tvLabel
            if (data.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
            tvLabel.text = data.data.label?.text ?: ""
            ivPicture.load(data.data.image, 4f)

            if (mDatas.size > 1) {
                val params = ivPicture.layoutParams as MarginLayoutParams
                params.leftMargin = dp2px(4f).toInt()
                params.rightMargin = dp2px(4f).toInt()
                params.topMargin = dp2px(4f).toInt()
                params.bottomMargin = dp2px(4f).toInt()
                ivPicture.layoutParams = params
            }
        }

        inner class BannerViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    }

    inner class SpecialSquareCardCollectionAdapter(val dataList: List<Discovery.ItemX>) :
        RecyclerView.Adapter<SpecialSquareCardCollectionAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivPicture = view.findViewById<ImageView>(R.id.ivPicture)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SpecialSquareCardCollectionAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_special_square_card_collection_type_item, parent, false)
            )
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(
            holder: SpecialSquareCardCollectionAdapter.ViewHolder,
            position: Int
        ) {
            val item = dataList[position]
            holder.ivPicture.load(item.data.image, 4f)
            holder.tvTitle.text = item.data.title
            holder.itemView.setOnClickListener {
                mContext.toast("${item.data.title},${mContext.getString(R.string.currently_not_supported)}")
            }
        }
    }

    inner class ColumnCardListAdapter(val dataList: List<Discovery.ItemX>) :
        RecyclerView.Adapter<ColumnCardListAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivPicture = view.findViewById<ImageView>(R.id.ivPicture)
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ColumnCardListAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_column_card_list_type_item, parent, false)
            )
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(holder: ColumnCardListAdapter.ViewHolder, position: Int) {
            val item = dataList[position]
            holder.ivPicture.load(item.data.image, 4f)
            holder.tvTitle.text = item.data.title
            holder.itemView.setOnClickListener {
                mContext.toast("${item.data.title},${mContext.getString(R.string.currently_not_supported)}")
            }
        }
    }

    inner class SpecialSquareCardCollectionItemDecoration() : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount //item count
            val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            val spanCount = 2
            val lastRowFirstItemPosition = count?.minus(spanCount)   //最后一行,第一个item索引
            val space = dp2px(2f).toInt()
            val rightCountSpace = dp2px(14f).toInt()

            when (spanIndex) {
                0 -> {
                    outRect.bottom = space
                }
                spanCount - 1 -> {
                    outRect.top = space
                }
                else -> {
                    outRect.top = space
                    outRect.bottom = space
                }
            }
            when {
                position < spanCount -> {
                    outRect.right = space
                }
                position < lastRowFirstItemPosition!! -> {
                    outRect.left = space
                    outRect.right = space
                }
                else -> {
                    outRect.left = space
                    outRect.right = rightCountSpace
                }
            }
        }
    }

}