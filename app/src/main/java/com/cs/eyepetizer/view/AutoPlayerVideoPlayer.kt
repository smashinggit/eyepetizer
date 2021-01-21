package com.cs.eyepetizer.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.cs.common.utils.Logs
import com.cs.common.utils.gone
import com.cs.eyepetizer.R
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.android.synthetic.main.layout_auto_play_video_player.view.*

/**
 * 常见列表，视频播放器
 */
class AutoPlayerVideoPlayer : StandardGSYVideoPlayer {

    constructor(context: Context) : super(context)

    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId() = R.layout.layout_auto_play_video_player

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
//        //不给触摸快进，如果需要，屏蔽下方代码即可
//        mChangePosition = false
//
//        //不给触摸音量，如果需要，屏蔽下方代码即可
//        mChangeVolume = false
//
//        //不给触摸亮度，如果需要，屏蔽下方代码即可
//        mBrightness = false
    }

    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> imageView.setImageResource(R.drawable.ic_pause_white_24dp)
                GSYVideoView.CURRENT_STATE_ERROR -> imageView.setImageResource(R.drawable.ic_play_white_24dp)
                else -> imageView.setImageResource(R.drawable.ic_play_white_24dp)
            }
        } else {
            super.updateStartImage()
        }
    }

    override fun touchDoubleUp() {
        super.touchDoubleUp();
        //不需要双击暂停
    }

    //正常
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        Logs.loge(javaClass.simpleName, "changeUiToNormal")
        mBottomContainer.gone()
    }

    //准备中
    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        Logs.loge(javaClass.simpleName, "changeUiToPreparingShow")
        mBottomContainer.gone()
    }

    //播放中
    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        Logs.loge(javaClass.simpleName, "changeUiToPlayingShow")
        mBottomContainer.gone()
        start.gone()
    }

    //开始缓冲
    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        Logs.loge(javaClass.simpleName, "changeUiToPlayingBufferingShow")
    }

    //暂停
    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        Logs.loge(javaClass.simpleName, "changeUiToPauseShow")
        mBottomContainer.gone()
    }

    //自动播放结束
    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        Logs.loge(javaClass.simpleName, "changeUiToCompleteShow")
    }

    //错误状态
    override fun changeUiToError() {
        super.changeUiToError()
        Logs.loge(javaClass.simpleName, "changeUiToError")
    }
}