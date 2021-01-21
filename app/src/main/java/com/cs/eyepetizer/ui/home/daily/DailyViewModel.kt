package com.cs.eyepetizer.ui.home.daily

import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.HomePageRepository
import com.cs.eyepetizer.repository.bean.Daily

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc
 **/
class DailyViewModel(private val repository: HomePageRepository) : ViewModel() {
    private var mUrl = MutableLiveData<String>()
    var mDataList = ArrayList<Daily.Item>()
    var mIsLoadMore = false
    var mNextPageUrl = ""


    val dailyResponse =
        Transformations.switchMap(mUrl) {
            liveData {
                try {
                    emit(Response.loading())
                    val homePageDaily = repository.homePageDaily(it)
                    emit(Response.success(homePageDaily))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Response.error<Daily>(e.toTip()))
                }
            }
        }


    fun refresh() {
        mIsLoadMore = false
        mUrl.value = Api.DAILY_URL
    }

    fun loadMore() {
        mIsLoadMore = true
        mUrl.value = mNextPageUrl
    }

}

class DailyViewModelFactory(private val repository: HomePageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DailyViewModel(repository) as T
    }
}