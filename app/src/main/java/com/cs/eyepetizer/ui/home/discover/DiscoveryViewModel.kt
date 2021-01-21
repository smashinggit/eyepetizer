package com.cs.eyepetizer.ui.home.discover

import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.HomePageRepository
import com.cs.eyepetizer.repository.bean.Discovery

/**
 *
 * @author  ChenSen
 * @date  2021/1/13
 * @desc
 **/
class DiscoveryViewModel(repository: HomePageRepository) : ViewModel() {
    private var mUrl = MutableLiveData<String>()
    var mDataList = ArrayList<Discovery.Item>()
    var mIsLoadMore = false
    var mNextPageUrl = ""

    var mDiscoveryResponse =
        Transformations.switchMap(mUrl) {
            liveData {

                try {
                    emit(Response.loading())
                    val discovery = repository.homePageDiscovery(it)
                    emit(Response.success(discovery))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Response.error<Discovery>(e.toTip()))
                }
            }
        }


    fun refresh() {
        mIsLoadMore = false
        mUrl.value = Api.DISCOVERY_URL
    }

    fun loadMore() {
        mIsLoadMore = true
        mUrl.value = mNextPageUrl
    }

}

class DiscoveryViewModelFactory(private val repository: HomePageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DiscoveryViewModel(repository) as T
    }

}