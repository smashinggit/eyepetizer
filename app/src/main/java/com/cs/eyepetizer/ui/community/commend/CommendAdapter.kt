package com.cs.eyepetizer.ui.community.commend

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.common.utils.*
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.CommunityRecommend
import com.cs.eyepetizer.ui.common.EmptyViewHolder
import com.cs.eyepetizer.ui.community.detail.CommunityDetailActivity
import com.cs.eyepetizer.ui.home.daily.DailyAdapter
import com.cs.eyepetizer.utils.ActionUrlUtil
import com.cs.eyepetizer.utils.setDrawable
import com.youth.banner.Banner
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_banner_item_type.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc
 **/
class CommendAdapter(val mContext: Context, var mData: List<CommunityRecommend.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //列表左or右间距
    val bothSideSpace = mContext.resources.getDimension(R.dimen.listSpaceSize).toInt()

    //列表中间内间距，左or右。
    val middleSpace = dp2px(3f).toInt()

    //通过获取屏幕宽度来计算出每张图片最大的宽度。
    val maxImageWidth = ((mContext.screenWidth() - bothSideSpace * 2 - middleSpace * 2) / 2).toInt()


    override fun getItemCount() = mData.size

    override fun getItemViewType(position: Int): Int {
        val item = mData[position]
        return when (item.type) {
            STR_HORIZONTAL_SCROLLCARD_TYPE -> {
                when (item.data.dataType) {
                    STR_ITEM_COLLECTION_DATA_TYPE -> HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE
                    STR_HORIZONTAL_SCROLLCARD_DATA_TYPE -> HORIZONTAL_SCROLLCARD_TYPE
                    else -> Constant.ItemViewType.UNKNOWN
                }
            }
            STR_COMMUNITY_COLUMNS_CARD -> {
                if (item.data.dataType == STR_FOLLOW_CARD_DATA_TYPE) FOLLOW_CARD_TYPE
                else Constant.ItemViewType.UNKNOWN
            }
            else -> Constant.ItemViewType.UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE -> {
            HorizontalScrollCardItemCollectionViewHolder(
                R.layout.item_community_horizontal_scrollcard_item_collection_type.inflate(
                    parent
                )
            )
        }
        HORIZONTAL_SCROLLCARD_TYPE -> {
            BannerViewHolder(
                R.layout.item_community_horizontal_scrollcard_type.inflate(
                    parent
                )
            )
        }
        FOLLOW_CARD_TYPE -> {
            FollowCardViewHolder(
                R.layout.item_community_columns_card_follow_card_type.inflate(
                    parent
                )
            )
        }
        else -> {
            EmptyViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mData[position]
        when (holder) {
            is HorizontalScrollCardItemCollectionViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true

                holder.recyclerView.layoutManager = LinearLayoutManager(mContext).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(
                        SquareCardOfCommunityContentItemDecoration(mContext)
                    )
                }
                holder.recyclerView.adapter =
                    SquareCardOfCommunityContentAdapter(mContext, item.data.itemList)
            }
            is BannerViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).apply {
                    isFullSpan = true
                }

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
            is FollowCardViewHolder -> {
                holder.tvChoiceness.gone()
                holder.ivPlay.gone()
                holder.ivLayers.gone()

                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.tvChoiceness.visible()

                if (item.data.header?.iconType ?: "".trim() == "round") {
                    holder.ivAvatar.invisible()
                    holder.ivRoundAvatar.visible()
                    holder.ivRoundAvatar.load(item.data.content.data.owner.avatar)
                } else {
                    holder.ivRoundAvatar.gone()
                    holder.ivAvatar.visible()
                    holder.ivAvatar.load(item.data.content.data.owner.avatar)
                }

                holder.ivBgPicture.run {

                    // 图片的宽度 = (屏幕宽度 - 两边的距离)/2
                    // 一行显示两张图片，所以这里除以2
                    val imageWidth =
                        (context.screenWidth() - bothSideSpace * 2 - middleSpace * 2) / 2

                    val imageHeight =
                        imageWidth * (item.data.content.data.height / item.data.content.data.width)
//                    log("result $imageWidth * $imageHeight")

                    layoutParams.width = imageWidth
                    layoutParams.height = if (imageHeight == 0) {
                        imageWidth
                    } else {
                        imageHeight
                    }

                    this.load(item.data.content.data.cover.feed, 4f)
                }
                holder.tvCollectionCount.text =
                    item.data.content.data.consumption.collectionCount.toString()
                val drawable = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_favorite_border_black_20dp
                )
                holder.tvCollectionCount.setDrawable(drawable, 17f, 17f, 2)
                holder.tvDescription.text = item.data.content.data.description
                holder.tvNickName.text = item.data.content.data.owner.nickname

                when (item.data.content.type) {
                    STR_VIDEO_TYPE -> {
                        holder.ivPlay.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                mData.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            CommunityDetailActivity.start(mContext, items, item)
                        }
                    }
                    STR_UGC_PICTURE_TYPE -> {
                        if (!item.data.content.data.urls.isNullOrEmpty() && item.data.content.data.urls.size > 1) holder.ivLayers.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                mData.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            CommunityDetailActivity.start(mContext, items, item)
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


    /**
     * 主题创作广场+话题讨论大厅……
     */
    inner class HorizontalScrollCardItemCollectionViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    }

    inner class SquareCardOfCommunityContentAdapter(
        val context: Context,
        var dataList: List<CommunityRecommend.ItemX>
    ) :
        RecyclerView.Adapter<SquareCardOfCommunityContentAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
            val tvTitle: TextView = view.findViewById(R.id.tvTitle)
            val tvSubTitle: TextView = view.findViewById(R.id.tvSubTitle)
        }

        override fun getItemCount() = dataList.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SquareCardOfCommunityContentAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_community_horizontal_scroll_card_itemcollection_item_type,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(
            holder: SquareCardOfCommunityContentAdapter.ViewHolder,
            position: Int
        ) {
            val item = dataList[position]
            holder.ivBgPicture.layoutParams.width = maxImageWidth
            holder.ivBgPicture.load(item.data.bgPicture)
            holder.tvTitle.text = item.data.title
            holder.tvSubTitle.text = item.data.subTitle
            holder.itemView.setOnClickListener {
                ActionUrlUtil.process(
                    context,
                    item.data.actionUrl,
                    item.data.title
                )
            }
        }
    }

    inner class SquareCardOfCommunityContentItemDecoration(val context: Context) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount ?: 1//item count

            when (position) {
                0 -> {
                    if (count == 2) {
                        outRect.right = middleSpace
                    }
                }
                count -> {

                }
                else -> {
                    outRect.left = middleSpace
                    outRect.right = middleSpace
                }
            }
        }
    }

    inner class HorizontalScrollCardAdapter :
        com.youth.banner.adapter.BannerAdapter<CommunityRecommend.ItemX, HorizontalScrollCardAdapter.HorizontalScrollCardHolder>(
            null
        ) {

        override fun onCreateHolder(parent: ViewGroup?, viewType: Int): HorizontalScrollCardHolder {
            val view =
                LayoutInflater.from(mContext).inflate(R.layout.item_banner_item_type, parent, false)
            return HorizontalScrollCardHolder(view)
        }

        override fun onBindView(
            holder: HorizontalScrollCardHolder,
            data: CommunityRecommend.ItemX,
            position: Int,
            size: Int
        ) {
            val ivPicture = holder.itemView.ivPicture
            val tvLabel = holder.itemView.tvLabel
            if (data.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
            tvLabel.text = data.data.label?.text ?: ""
            ivPicture.load(data.data.image, 4f)
        }

        inner class HorizontalScrollCardHolder(val view: View) : RecyclerView.ViewHolder(view)
    }

    inner class BannerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val banner: Banner<CommunityRecommend.ItemX, HorizontalScrollCardAdapter> =
            view.findViewById(R.id.banner)
    }


    inner class FollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
        val tvChoiceness: TextView = view.findViewById(R.id.tvChoiceness)
        val ivLayers: ImageView = view.findViewById(R.id.ivLayers)
        val ivPlay: ImageView = view.findViewById(R.id.ivPlay)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val ivRoundAvatar: CircleImageView = view.findViewById(R.id.ivRoundAvatar)
        val tvNickName: TextView = view.findViewById(R.id.tvNickName)
        val tvCollectionCount: TextView = view.findViewById(R.id.tvCollectionCount)
    }


    companion object {
        const val TAG = "CommendAdapter"

        const val STR_HORIZONTAL_SCROLLCARD_TYPE = "horizontalScrollCard"
        const val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"

        const val STR_HORIZONTAL_SCROLLCARD_DATA_TYPE = "HorizontalScrollCard"
        const val STR_ITEM_COLLECTION_DATA_TYPE = "ItemCollection"
        const val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"

        const val STR_VIDEO_TYPE = "video"
        const val STR_UGC_PICTURE_TYPE = "ugcPicture"

        //type:horizontalScrollCard -> dataType:ItemCollection
        const val HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE = 1

        //type:horizontalScrollCard -> dataType:HorizontalScrollCard
        const val HORIZONTAL_SCROLLCARD_TYPE = 2

        //type:communityColumnsCard -> dataType:FollowCard
        const val FOLLOW_CARD_TYPE = 3
    }
}