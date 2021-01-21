package com.cs.eyepetizer.ui.notification.push

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.cs.common.http.Response
import com.cs.common.http.toTip
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.NotificationRepository
import com.cs.eyepetizer.repository.bean.Discovery
import com.cs.eyepetizer.repository.bean.PushMessage

/**
 *
 * @author  ChenSen
 * @date  2021/1/15
 * @desc
 **/
class PushViewModel(private val repository: NotificationRepository) : ViewModel() {

    private var mUrl = MutableLiveData<String>()
    var mDataList = ArrayList<PushMessage.Message>()
    var mIsLoadMore = false
    var mNextPageUrl = ""

    val pushMessageResponse =
        Transformations.switchMap(mUrl) {
            liveData {
                try {
                    emit(Response.loading())
                    val pushMessage = repository.refreshPushMessage(it)
                    emit(Response.success(pushMessage))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Response.error<PushMessage>(e.toTip()))
                }
            }
        }


    fun refresh() {
        mIsLoadMore = false
        mUrl.value = Api.PUSHMESSAGE_URL
    }

    fun loadMore() {
        mIsLoadMore = true
        mUrl.value = mNextPageUrl
    }


}


class PushViewModelFactory(private val repository: NotificationRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PushViewModel(repository) as T

    }
}
