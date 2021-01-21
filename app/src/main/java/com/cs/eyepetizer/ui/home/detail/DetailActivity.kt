package com.cs.eyepetizer.ui.home.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.common.http.Response
import com.cs.common.utils.*
import com.cs.common.utils.Shares.SHARE_WECHAT
import com.cs.common.utils.Shares.SHARE_WECHAT_MEMORIES
import com.cs.common.utils.Shares.SHARE_WEIBO
import com.cs.common.utils.Shares.share
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import com.cs.eyepetizer.repository.Injectors
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.utils.showShareDialog
import com.cs.eyepetizer.view.LoadMoreFooter
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 **/
class DetailActivity : BaseActivity() {
    protected val TAG: String = this.javaClass.simpleName

    private var hideTitleJob: Job? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            Injectors.getDetailViewModelFactory()
        ).get(DetailViewModel::class.java)
    }

    private val mRelatedAdapter by lazy {
        DetailRelatedAdapter(this, viewModel.mVideoRelatedData, viewModel.mVideoInfo)
    }

    private val mReplyAdapter by lazy {
        DetailReplyAdapter(this, viewModel.mVideoRepliedData)
    }

    private val mMergedAdapter by lazy {
        ConcatAdapter(mRelatedAdapter, mReplyAdapter)
    }

    private val mOrientationUtils by lazy {
        OrientationUtils(this, videoPlayer)
    }

    override fun setLayoutRes() = R.layout.activity_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getParcelableExtra<VideoInfo>(EXTRA_VIDEOINFO)?.let {
            viewModel.mVideoInfo = it
            viewModel.mVideoId = it.videoId
            log("mVideoInfo $it")
        }

        intent.getLongExtra(EXTRA_VIDEO_ID, -1L).let {
            if (it != -1L) {
                viewModel.mVideoId = it
                log("mVideoId $it")
            }
        }

        super.onCreate(savedInstanceState)

        if (viewModel.mVideoInfo != null) {  //已经有视频信息(由调用方传入)
            startVideoPlayer()
        }

        viewModel.onRefresh()
    }

    override fun initView() {
        super.initView()
        setStatusBarBackground(R.color.black)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = mMergedAdapter
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = null

        refreshLayout.run {
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            refreshLayout.setEnableNestedScroll(true)
            setFooterHeight(153f)
            setRefreshFooter(LoadMoreFooter(this@DetailActivity).apply {
                setTitleColor(Color.WHITE)
                setNoMoreTipColor(Color.WHITE)
            })

            setOnRefreshListener { finish() }
            setOnLoadMoreListener { viewModel.onLoadMore() }
        }

        setOnClickListener(
            ivPullDown,
            ivMore,
            ivShare,
            ivCollection,
            ivToWechatFriends,
            ivShareToWechatMemories,
            ivShareToWeibo,
            ivShareToQQ,
            ivShareToQQzone,
            ivAvatar,
            etComment,
            ivReply,
            tvReplyCount
        ) {

            viewModel.mVideoInfo?.let {
                when (this) {
                    ivPullDown -> finish()
                    ivMore -> {
                    }
                    ivShare -> showShareDialog(this@DetailActivity, it.webUrl.raw)
                    ivCollection -> LoginActivity.start(this@DetailActivity)
                    ivToWechatFriends -> share(this@DetailActivity, it.webUrl.raw, SHARE_WECHAT)
                    ivShareToWechatMemories -> share(
                        this@DetailActivity,
                        it.webUrl.raw,
                        SHARE_WECHAT_MEMORIES
                    )
                    ivShareToWeibo -> share(this@DetailActivity, it.webUrl.forWeibo, SHARE_WEIBO)
                    ivAvatar, etComment -> LoginActivity.start(this@DetailActivity)
                    ivReply, tvReplyCount -> scrollRepliesTop()
                    else -> {
                    }
                }
            }
        }
    }

    override fun observer() {

        viewModel.mVideoDetailResponse.observe(this, Observer { it ->
            when (it.state) {
                Response.State.LOADING -> {
                }

                Response.State.SUCCESS -> {
                    val response = it.data!!
                    viewModel.mNextRepliesUrl = response.videoReplies.nextPageUrl
                    response.videoBeanForClient?.run {
                        viewModel.mVideoInfo = VideoInfo(
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
                        startVideoPlayer()
                        mRelatedAdapter.bindVideoInfo(viewModel.mVideoInfo)
                    }

                    response.videoRelated?.itemList?.let { related ->
                        viewModel.mVideoRelatedData.clear()
                        viewModel.mVideoRelatedData.addAll(related)
                        mRelatedAdapter.notifyDataSetChanged()
                    }

                    response.videoReplies.itemList.let { replies ->
                        viewModel.mVideoRepliedData.clear()
                        viewModel.mVideoRepliedData.addAll(replies)
                        mReplyAdapter.notifyDataSetChanged()
                    }

                    when {
                        viewModel.mVideoRepliedData.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                        response.videoReplies.nextPageUrl.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                        else -> refreshLayout.closeHeaderOrFooter()
                    }
                }

                Response.State.ERROR -> {
                    toast(it.message ?: "")
                }
            }

        })

        viewModel.mRepliesAndRelatedResponse.observe(this, Observer {
            when (it.state) {
                Response.State.LOADING -> {
                }

                Response.State.SUCCESS -> {
                    val response = it.data!!
                    viewModel.mNextRepliesUrl = response.videoReplies.nextPageUrl

                    response.videoRelated?.itemList?.let { related ->
                        viewModel.mVideoRelatedData.clear()
                        viewModel.mVideoRelatedData.addAll(related)
                        mRelatedAdapter.notifyDataSetChanged()
                    }

                    response.videoReplies.itemList.let { replies ->
                        viewModel.mVideoRepliedData.clear()
                        viewModel.mVideoRepliedData.addAll(replies)
                        mReplyAdapter.notifyDataSetChanged()
                    }

                    when {
                        viewModel.mVideoRepliedData.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                        response.videoReplies.nextPageUrl.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                        else -> refreshLayout.closeHeaderOrFooter()
                    }
                }

                Response.State.ERROR -> {
                    hideLoading()
                    toast(it.message ?: "")
                }
            }

        })

        viewModel.mMoreRepliesResponse.observe(this, Observer {
            when (it.state) {
                Response.State.LOADING -> {
                }

                Response.State.SUCCESS -> {
                    val response = it.data!!
                    viewModel.mNextRepliesUrl = response.nextPageUrl

                    val itemCount = mReplyAdapter.itemCount
                    viewModel.mVideoRepliedData.addAll(response.itemList)
                    mReplyAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)

                    if (response.nextPageUrl.isNullOrEmpty()) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.closeHeaderOrFooter()
                    }
                }

                Response.State.ERROR -> {
                    hideLoading()
                    toast(it.message ?: "")
                }
            }
        })
    }


    private fun scrollRepliesTop() {
        val targetPostion = (mRelatedAdapter.itemCount - 1) + 2  //+相关推荐最后一项，+1评论标题，+1条评论
        if (targetPostion < mMergedAdapter.itemCount - 1) {
            recyclerView.smoothScrollToPosition(targetPostion)
        }
    }

    private fun startVideoPlayer() {
        viewModel.mVideoInfo?.let { videoInfo ->
            ivBlurredBg.load(videoInfo.cover.blurred)
            tvReplyCount.text = videoInfo.consumption.replyCount.toString()

            videoPlayer.apply {
                //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
                fullscreenButton.setOnClickListener { showFull() }
                //防止错位设置
                playTag = TAG
                //音频焦点冲突时是否释放
                isReleaseWhenLossAudio = false
                //增加封面
                thumbImageView = ImageView(this@DetailActivity).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    load(videoInfo.cover.detail)
                }
                thumbImageView.setOnClickListener { switchTitleBarVisible() }
                //是否开启自动旋转
                isRotateViewAuto = false
                //是否需要全屏锁定屏幕功能
                isNeedLockFull = true
                //是否可以滑动调整
                setIsTouchWiget(true)
                //设置触摸显示控制ui的消失时间
                dismissControlTime = 5000
                //设置播放过程中的回调
                setVideoAllCallBack(VideoCallPlayBack())
                //设置播放URL
                setUp(videoInfo.playUrl, false, videoInfo.title)
                //开始播放
                startPlayLogic()
            }
        }
    }

    private fun showFull() {
        mOrientationUtils.run { if (isLand != 1) resolveByClick() }
        videoPlayer.startWindowFullscreen(this, true, false)
    }

    private fun switchTitleBarVisible() {
        if (videoPlayer.currentPlayer.currentState == GSYVideoView.CURRENT_STATE_AUTO_COMPLETE) return
        if (flHeader.visibility == View.VISIBLE) {
            hideTitleJob?.cancel()
            hideTitleBar()
        } else {
            flHeader.visibleWithAlpha(1000)
            ivPullDown.visibleWithAlpha(1000)
            ivCollection.visibleWithAlpha(1000)
            ivMore.visibleWithAlpha(1000)
            ivShare.visibleWithAlpha(1000)
            delayHideTitleBar()
        }
    }

    private fun delayHideTitleBar() {
        hideTitleJob?.cancel()
        hideTitleJob = lifecycleScope.launch {
            delay(videoPlayer.dismissControlTime.toLong())
            hideTitleBar()
        }
    }

    private fun hideTitleBar() {
        flHeader.invisibleWithAlpha(1000)
        ivPullDown.goneWithAlpha(1000)
        ivCollection.goneWithAlpha(1000)
        ivMore.goneWithAlpha(1000)
        ivShare.goneWithAlpha(1000)
    }


    private fun delayHideBottomContainer() {
        lifecycleScope.launch {
            delay(videoPlayer.dismissControlTime.toLong())
            videoPlayer.getBottomContainer().gone()
            videoPlayer.startButton.gone()
        }
    }

    inner class VideoCallPlayBack : GSYSampleCallBack() {
        override fun onStartPrepared(url: String?, vararg objects: Any?) {
            super.onStartPrepared(url, *objects)
            flHeader.gone()
            llShares.gone()
        }

        override fun onClickBlank(url: String?, vararg objects: Any?) {
            super.onClickBlank(url, *objects)
            switchTitleBarVisible()
        }

        override fun onClickStop(url: String?, vararg objects: Any?) {
            super.onClickStop(url, *objects)
            delayHideBottomContainer()
        }

        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            super.onAutoComplete(url, *objects)
            flHeader.visible()
            ivPullDown.visible()
            ivCollection.gone()
            ivShare.gone()
            ivMore.gone()
            llShares.visible()
        }
    }


    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

        GSYVideoADManager.releaseAllVideos()
        mOrientationUtils.releaseListener()
        videoPlayer.release()
        videoPlayer.setVideoAllCallBack(null)
    }

    override fun onBackPressed() {
        mOrientationUtils.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    fun scrollTop() {
        if (mRelatedAdapter.itemCount != 0) {
            recyclerView.scrollToPosition(0)
            refreshLayout.invisibleWithAlpha(2500)
            refreshLayout.visibleWithAlpha(1500)
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        log("onNewIntent")
//        initParams()
//        startVideoPlayer()
//        viewModel.onRefresh()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        log("onConfigurationChanged")
        videoPlayer.onConfigurationChanged(this, newConfig, mOrientationUtils, true, true)
    }


    companion object {

        const val EXTRA_VIDEOINFO = "videoInfo"
        const val EXTRA_VIDEO_ID = "videoId"

        fun start(context: Context, videoInfo: VideoInfo) {
            val starter = Intent(context, DetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEOINFO, videoInfo)
            context.startActivity(starter)
        }

        fun start(context: Context, videoId: Long) {
            val starter = Intent(context, DetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEO_ID, videoId)
            context.startActivity(starter)
        }
    }
}