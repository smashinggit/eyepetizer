package com.cs.eyepetizer.ui.community.commend

import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.CommunityRepository
import com.cs.eyepetizer.repository.bean.Commend
import com.cs.eyepetizer.repository.bean.CommunityRecommend

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc
 **/
class CommendViewModel(private val repository: CommunityRepository) : ViewModel() {

    private var mUrl = MutableLiveData<String>()
    var mDataList = ArrayList<CommunityRecommend.Item>()
    var mIsLoadMore = false
    var mNextPageUrl = ""

    val communityRecommend =
        Transformations.switchMap(mUrl) {
            liveData {
                try {
                    emit(Response.loading())
                    val communityRecommend = repository.communityRecommend(mUrl.value ?: "")
                    emit(Response.success(communityRecommend))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Response.error<CommunityRecommend>(e.toTip()))
                }
            }
        }


    fun onRefresh() {
        mUrl.value = Api.COMMUNITY_RECOMMEND_URL
        mIsLoadMore = false
    }

    fun onLoadMore() {
        mUrl.value = mNextPageUrl
        mIsLoadMore = true
    }
}

class CommendViewModelFactory(private val repository: CommunityRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommendViewModel(repository) as T
    }
}