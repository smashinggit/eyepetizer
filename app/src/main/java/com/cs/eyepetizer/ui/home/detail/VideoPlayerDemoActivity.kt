package com.cs.eyepetizer.ui.home.detail

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.activity_detail.*


/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc  视频播放的demo代码
 **/
class VideoPlayerDemoActivity : BaseActivity() {

    private lateinit var mOrientationUtils: OrientationUtils

    override fun setLayoutRes() = R.layout.activity_video_demo

    override fun initView() {

        val source1 =
            "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"


        videoPlayer.setUp(source1, true, "测试视频")


        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(R.mipmap.ic_launcher)
        videoPlayer.thumbImageView = imageView

        //增加title
        videoPlayer.titleTextView.visibility = View.VISIBLE
        //设置返回键
        videoPlayer.backButton.visibility = View.VISIBLE

        //设置旋转
        mOrientationUtils = OrientationUtils(this, videoPlayer)

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.fullscreenButton.setOnClickListener {
            mOrientationUtils.resolveByClick()
        }

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)
        //设置返回按键功能

        videoPlayer.backButton.setOnClickListener { onBackPressed() }
        videoPlayer.startPlayLogic()
    }


    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }


    override fun onDestroy() {
        GSYVideoManager.releaseAllVideos()
        mOrientationUtils.releaseListener();
        super.onDestroy()
    }


    override fun onBackPressed() {
        //先返回正常状态
        if (mOrientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.fullscreenButton.performClick()
            return
        }

        //释放所有
        videoPlayer.setVideoAllCallBack(null);

        super.onBackPressed()
    }
}


