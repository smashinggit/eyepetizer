package com.cs.eyepetizer.ui.home.detail

import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.eyepetizer.network.api.VideoApi
import com.cs.eyepetizer.repository.VideoRepository
import com.cs.eyepetizer.repository.bean.VideoDetail
import com.cs.eyepetizer.repository.bean.VideoInfo
import com.cs.eyepetizer.repository.bean.VideoRelated
import com.cs.eyepetizer.repository.bean.VideoReplies
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *
 * @author  ChenSen
 * @date  2021/1/18
 * @desc
 **/
class DetailViewModel(private val repository: VideoRepository) : ViewModel() {

    private val mRepliesUrl = MutableLiveData<String>()
    val mVideoRelatedData = ArrayList<VideoRelated.Item>()
    val mVideoRepliedData = ArrayList<VideoReplies.Item>()

    var mNextRepliesUrl: String? = null
    var mVideoInfo: VideoInfo? = null
    var mVideoId = 0L

    val mVideoDetailResponse = MutableLiveData<Response<VideoDetail>>()
    val mRepliesAndRelatedResponse = MutableLiveData<Response<VideoDetail>>()

    val mMoreRepliesResponse = Transformations.switchMap(mRepliesUrl) {
        liveData {
            try {
                emit(Response.loading())
                val videoReplies = repository.refreshVideoReplies(it)
                emit(Response.success(videoReplies))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.error<VideoReplies>(e.toTip()))
            }
        }
    }


    fun getVideoDetail(videoId: Long, repliesUrl: String) {
        viewModelScope.launch {
            try {
                mVideoDetailResponse.postValue(Response.loading())

                val videoBean = async { repository.refreshVideoBean(videoId) }
                val videoRelated = async { repository.refreshVideoRelated(videoId) }
                val videoReplies = async { repository.refreshVideoReplies(repliesUrl) }

                val videoDetail =
                    VideoDetail(videoBean.await(), videoRelated.await(), videoReplies.await())
                mVideoDetailResponse.postValue(Response.success(videoDetail))
            } catch (e: Exception) {
                e.printStackTrace()
                mVideoDetailResponse.postValue(Response.error<VideoDetail>(e.toTip()))
            }
        }
    }

    fun getRepliesAndRelated(videoId: Long, repliesUrl: String) {

        viewModelScope.launch {
            try {
                mRepliesAndRelatedResponse.postValue(Response.loading())
                val videoRelated = async { repository.refreshVideoRelated(videoId) }
                val videoReplies = async { repository.refreshVideoReplies(repliesUrl) }

                val videoDetail =
                    VideoDetail(null, videoRelated.await(), videoReplies.await())
                mRepliesAndRelatedResponse.postValue(Response.success(videoDetail))
            } catch (e: Exception) {
                e.printStackTrace()
                mRepliesAndRelatedResponse.postValue(Response.error<VideoDetail>(e.toTip()))
            }
        }
    }


    fun onRefresh() {
        if (mVideoInfo == null) {
            getVideoDetail(mVideoId, "${VideoApi.VIDEO_REPLIES_URL}$mVideoId")
        } else {// 说明VideoInfo已经纯在，只需要获取评论和推荐
            getRepliesAndRelated(
                mVideoInfo!!.videoId,
                "${VideoApi.VIDEO_REPLIES_URL}${mVideoInfo!!.videoId}"
            )
        }
    }

    fun onLoadMore() {
        mRepliesUrl.value = mNextRepliesUrl ?: ""
    }
}

class DetailViewModelFactory(private val repository: VideoRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(repository) as T

    }
}