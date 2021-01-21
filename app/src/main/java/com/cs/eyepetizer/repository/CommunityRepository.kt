package com.cs.eyepetizer.repository

import com.cs.eyepetizer.network.RetrofitClient
import com.cs.eyepetizer.network.api.Api
import com.cs.eyepetizer.repository.bean.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/11
 * @desc 社区仓库
 **/
class CommunityRepository private constructor(
    private val api: Api = RetrofitClient.create(Api::class.java)
) {

    suspend fun communityRecommend(url: String): CommunityRecommend {
        return api.getCommunityRecommend(url)
    }

    suspend fun communityFollow(url: String): Follow {
        return api.getCommunityFollow(url)
    }


    companion object {
        private var repository: CommunityRepository? = null

        fun getInstance(api: Api): CommunityRepository {

            return repository ?: synchronized(this) {
                CommunityRepository(api).apply {
                    repository = this
                }
            }
        }
    }


}
