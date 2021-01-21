package com.cs.eyepetizer.ui.community.follow

import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.CommunityRepository
import com.cs.eyepetizer.repository.bean.Follow

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc
 **/
class FollowViewModel(private val repository: CommunityRepository) : ViewModel() {

    private var mUrl = MutableLiveData<String>()
    var mDataList = ArrayList<Follow.Item>()
    var mIsLoadMore = false
    var mNextPageUrl = ""

    val followResponse = Transformations.switchMap(mUrl) {
        liveData {
            try {
                emit(Response.loading())
                val communityFollow = repository.communityFollow(it)
                emit(Response.success(communityFollow))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Response.error<Follow>(e.toTip()))
            }
        }
    }


    fun onRefresh() {
        mUrl.value = Api.FOLLOW_URL
        mIsLoadMore = false
    }

    fun onLoadMore() {
        mUrl.value = mNextPageUrl
        mIsLoadMore = true
    }

}

class FollowViewModelFactory(private val repository: CommunityRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowViewModel(repository) as T
    }
}