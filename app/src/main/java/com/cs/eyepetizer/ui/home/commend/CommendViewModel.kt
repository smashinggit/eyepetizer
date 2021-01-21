package com.cs.eyepetizer.ui.home.commend

import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.common.utils.launch
import com.cs.eyepetizer.repository.bean.Commend
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.HomePageRepository

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc
 **/
class CommendViewModel(private val repository: HomePageRepository) : ViewModel() {

    private var mUrl = MutableLiveData<String>()
    var mDataList = ArrayList<Commend.Item>()
    var mIsLoadMore = false
    var mNextPageUrl = ""

    //写法2
    val commendPageLiveData2 =
        Transformations.switchMap(mUrl) {
            liveData {
                try {
                    emit(Response.loading())

                    val commendPage = repository.homePageRecommend(mUrl.value ?: "")
                    emit(Response.success(commendPage))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Response.error<Commend>(e.toTip()))
                }
            }
        }

    //写法1
    val commendPageLiveData = MutableLiveData<Response<Commend>>()
    fun getHomePageRecommend() {
        launch({
            commendPageLiveData.postValue(Response.loading())
            val commendPage = repository.homePageRecommend(mUrl.value ?: "")
            commendPageLiveData.postValue(Response.success(commendPage))

        }, onError = {
            commendPageLiveData.postValue(Response.error(it.message ?: "未知错误"))
        })
    }


    fun onRefresh() {
        mUrl.value = Api.HOMEPAGE_RECOMMEND_URL
        mIsLoadMore = false
    }

    fun onLoadMore() {
        mUrl.value = mNextPageUrl
        mIsLoadMore = true
    }

}

class CommendViewModelFactory(private val repository: HomePageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommendViewModel(repository) as T
    }
}