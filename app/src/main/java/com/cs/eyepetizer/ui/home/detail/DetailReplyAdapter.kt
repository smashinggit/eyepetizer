package com.cs.eyepetizer.ui.home.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.*
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.VideoReplies
import com.cs.eyepetizer.ui.common.ItemTypes
import com.cs.eyepetizer.ui.common.TextCardViewHeader4ViewHolder
import com.cs.eyepetizer.ui.common.ViewHolders

/**
 *
 * @author  ChenSen
 * @date  2021/1/19
 * @desc
 **/
class DetailReplyAdapter(
    private val mContext: Context, private val mData: List<VideoReplies.Item>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            REPLY_BEAN_FOR_CLIENT_TYPE -> ReplyViewHolder(
                R.layout.item_new_detail_reply_type.inflate(
                    parent
                )
            )
            else -> ViewHolders.getViewHolder(parent, viewType)
        }
    }

    override fun getItemCount() = mData.size
    override fun getItemViewType(position: Int) = when {
        mData[position].type == "reply" && mData[position].data.dataType == "ReplyBeanForClient"
        -> REPLY_BEAN_FOR_CLIENT_TYPE
        else -> ItemTypes.getItemViewType(mData[position])
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mData[position]
        when (holder) {
            is TextCardViewHeader4ViewHolder -> {
                holder.tvTitle4.text = item.data.text
                if (item.data.actionUrl != null && item.data.actionUrl.startsWith(Constant.ActionUrl.REPLIES_HOT)) {
                    //热门评论
                    holder.ivInto4.visible()
                    holder.tvTitle4.layoutParams =
                        (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(
                                0,
                                dp2px(30f).toInt(),
                                0,
                                dp2px(6f).toInt()
                            )
                        }
                    setOnClickListener(holder.tvTitle4, holder.ivInto4) {
                        mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                    }
                } else {
                    //相关推荐、最新评论
                    holder.tvTitle4.layoutParams =
                        (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(
                                0,
                                dp2px(24f).toInt(),
                                0,
                                dp2px(6f).toInt()
                            )
                        }
                    holder.ivInto4.gone()
                }
            }
            is ReplyViewHolder -> {
                holder.groupReply.gone()
                holder.ivAvatar.load(item.data.user?.avatar ?: "")
                holder.tvNickName.text = item.data.user?.nickname
                holder.tvLikeCount.text =
                    if (item.data.likeCount == 0) "" else item.data.likeCount.toString()
                holder.tvMessage.text = item.data.message
                holder.tvTime.text = getTimeReply(item.data.createTime)
                holder.ivLike.setOnClickListener {
                    mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                }
                item.data.parentReply?.run {
                    holder.groupReply.visible()
                    holder.tvReplyUser.text =
                        String.format(
                            mContext.resources.getString(R.string.reply_target),
                            user?.nickname
                        )
                    holder.ivReplyAvatar.load(user?.avatar ?: "")
                    holder.tvReplyNickName.text = user?.nickname
                    holder.tvReplyMessage.text = message
                    holder.tvShowConversation.setOnClickListener {
                        mContext.toast(mContext.resources.getString(R.string.currently_not_supported))
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }

    private fun getTimeReply(dateMillis: Long): String {
        val currentMillis = System.currentTimeMillis()
        val timePast = currentMillis - dateMillis
        // 采用误差一分钟以内的算法，防止客户端和服务器时间不同步导致的显示问题
        return if (timePast > -Dates.VALUE_MINUTE) when {
            timePast < Dates.VALUE_DAY -> {
                var pastHours = timePast / Dates.VALUE_HOUR
                if (pastHours <= 0) {
                    pastHours = 1
                }
                Dates.getDate(dateMillis, "HH:mm")
            }
            else -> Dates.getDate(dateMillis, "yyyy/MM/dd")
        } else {
            Dates.getDate(dateMillis, "yyyy/MM/dd HH:mm")
        }
    }


    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvNickName = view.findViewById<TextView>(R.id.tvNickName)
        val ivLike = view.findViewById<ImageView>(R.id.ivLike)
        val tvLikeCount = view.findViewById<TextView>(R.id.tvLikeCount)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)

        val groupReply = view.findViewById<Group>(R.id.groupReply)
        val tvReplyUser = view.findViewById<TextView>(R.id.tvReplyUser)
        val ivReplyAvatar = view.findViewById<ImageView>(R.id.ivReplyAvatar)
        val tvReplyNickName = view.findViewById<TextView>(R.id.tvReplyNickName)
        val tvReplyMessage = view.findViewById<TextView>(R.id.tvReplyMessage)
        val tvShowConversation = view.findViewById<TextView>(R.id.tvShowConversation)
    }

    companion object {
        const val TAG = "NewDetailReplyAdapter"
        const val REPLY_BEAN_FOR_CLIENT_TYPE = Constant.ItemViewType.MAX
    }
}