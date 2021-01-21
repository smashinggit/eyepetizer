package com.cs.eyepetizer.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cs.common.utils.Shares.SHARE_MORE
import com.cs.common.utils.Shares.SHARE_QQ
import com.cs.common.utils.Shares.SHARE_QQZONE
import com.cs.common.utils.Shares.SHARE_WECHAT
import com.cs.common.utils.Shares.SHARE_WEIBO
import com.cs.common.utils.Shares.share
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.utils.setDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_share_dialog.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 **/
class ShareDialogFragment : BottomSheetDialogFragment() {

    private lateinit var shareContent: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_dialog, container, false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        activity?.let { activity ->
            tvToWechatFriends.setDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.ic_share_wechat_black_30dp
                ), 30f, 30f, 1
            )
            tvShareToWeibo.setDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.ic_share_weibo_black_30dp
                ), 30f, 30f, 1
            )
            tvShareToQQ.setDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.ic_share_qq_black_30dp
                ), 30f, 30f, 1
            )
            tvShareToQQzone.setDrawable(
                ContextCompat.getDrawable(
                    activity,
                    R.drawable.ic_share_qq_zone_black_30dp
                ), 30f, 30f, 1
            )

            tvShareToQQ.setOnClickListener {
                share(activity, shareContent, SHARE_QQ)
                dismiss()
            }
            tvToWechatFriends.setOnClickListener {
                share(activity, shareContent, SHARE_WECHAT)
                dismiss()
            }
            tvShareToWeibo.setOnClickListener {
                share(activity, shareContent, SHARE_WEIBO)
                dismiss()
            }
            tvShareToQQzone.setOnClickListener {
                share(activity, shareContent, SHARE_QQZONE)
                dismiss()
            }
            llMore.setOnClickListener {
                share(activity, shareContent, SHARE_MORE)
                dismiss()
            }
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    fun showDialog(activity: AppCompatActivity, shareContent: String) {
        MobclickAgent.onEvent(activity, Constant.Mobclick.EVENT_SHARE)
        show(activity.supportFragmentManager, "share_dialog")
        this.shareContent = shareContent
    }
}