package com.cs.eyepetizer.ui.notification.push

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cs.common.utils.Dates
import com.cs.common.utils.inflate
import com.cs.common.utils.load
import com.cs.eyepetizer.R
import com.cs.eyepetizer.repository.bean.PushMessage
import com.cs.eyepetizer.utils.ActionUrlUtil
import kotlinx.android.synthetic.main.item_notification_push.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/15
 * @desc
 **/
class PushAdapter(
    private val mContext: Context,
    private val mData: List<PushMessage.Message>
) : RecyclerView.Adapter<PushAdapter.PushViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PushViewHolder {
        return PushViewHolder(R.layout.item_notification_push.inflate(parent))
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: PushViewHolder, position: Int) {

        mData[position].run {
            holder.itemView.ivAvatar.load(icon) { error(R.mipmap.ic_launcher) }
            holder.itemView.tvTitle.text = title
            holder.itemView.tvTime.text = Dates.getConvertedDate(date)
            holder.itemView.tvContent.text = content
        }

        holder.itemView.setOnClickListener {
            val item = mData[position]
            ActionUrlUtil.process(mContext, item.actionUrl, item.title)
        }
    }


    inner class PushViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}