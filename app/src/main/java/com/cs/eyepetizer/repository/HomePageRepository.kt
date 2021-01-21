package com.cs.eyepetizer.repository

import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.bean.Commend
import com.cs.eyepetizer.repository.bean.Daily
import com.cs.eyepetizer.repository.bean.Discovery

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc
 **/
class HomePageRepository private constructor(
    private val api: Api = RetrofitClient.create(Api::class.java)
) {

    suspend fun homePageDiscovery(url: String): Discovery {
        return api.getDiscovery(url)
    }

    suspend fun homePageRecommend(url: String): Commend {
        return api.getHomePageRecommend(url)
    }


    suspend fun homePageDaily(url: String): Daily {
        return api.getDaily(url)
    }


    companion object {
        private var repository: HomePageRepository? = null

        fun getInstance(api: Api): HomePageRepository {

            return repository ?: synchronized(this) {
                HomePageRepository(api).apply {
                    repository = this
                }
            }
        }
    }


}
