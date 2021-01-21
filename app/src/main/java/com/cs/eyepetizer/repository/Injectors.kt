package com.cs.eyepetizer.repository

import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.network.api.VideoApi
import com.cs.eyepetizer.ui.community.follow.FollowViewModelFactory
import com.cs.eyepetizer.ui.home.detail.DetailViewModelFactory
import com.cs.eyepetizer.ui.home.commend.CommendViewModelFactory
import com.cs.eyepetizer.ui.home.daily.DailyViewModelFactory
import com.cs.eyepetizer.ui.home.discover.DiscoveryViewModelFactory
import com.cs.eyepetizer.ui.notification.push.PushViewModelFactory

/**
 *
 * @author  ChenSen
 * @date  2021/1/12
 * @desc
 **/
object Injectors {

    private fun getApi() = RetrofitClient.create(Api::class.java)
    private fun getVideoApi() = RetrofitClient.create(VideoApi::class.java)

    //首页相关
    private fun getHomePageRepository() = HomePageRepository.getInstance(getApi())
    fun getHomeRecommendViewModelFactory() = CommendViewModelFactory(getHomePageRepository())
    fun getHomeDiscoveryViewModelFactory() = DiscoveryViewModelFactory(getHomePageRepository())
    fun getHomeDailyViewModelFactory() = DailyViewModelFactory(getHomePageRepository())

    //社区
    private fun getCommunityRepository() = CommunityRepository.getInstance(getApi())
    fun getCommunityCommendViewModelFactory() =
        com.cs.eyepetizer.ui.community.commend.CommendViewModelFactory(getCommunityRepository())

    fun getCommunityFollowViewModelFactory() = FollowViewModelFactory(getCommunityRepository())


    //通知
    private fun getNotificationRepository() = NotificationRepository.getInstance(getApi())
    fun getPushViewModelFactory() = PushViewModelFactory(getNotificationRepository())


    //视频详情
    private fun getVideoRepository() = VideoRepository.getInstance(getVideoApi())
    fun getDetailViewModelFactory() = DetailViewModelFactory(getVideoRepository())

}