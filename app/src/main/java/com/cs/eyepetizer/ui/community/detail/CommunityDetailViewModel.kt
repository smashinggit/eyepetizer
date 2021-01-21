package com.cs.eyepetizer.ui.community.detail

import androidx.lifecycle.ViewModel
import com.cs.eyepetizer.repository.bean.CommunityRecommend

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc
 **/
class CommunityDetailViewModel : ViewModel() {

    var dataList: List<CommunityRecommend.Item> = arrayListOf()
    var itemPosition: Int = -1
}
