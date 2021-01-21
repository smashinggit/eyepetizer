package com.cs.eyepetizer.ui.community

import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.base.BaseViewPagerFragment
import com.cs.eyepetizer.ui.community.commend.CommendFragment
import com.cs.eyepetizer.ui.community.follow.FollowFragment

/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc : 社区
 **/
class CommunityFragment : BaseViewPagerFragment() {

    private val mFragments by lazy {
        return@lazy arrayListOf(
            CommendFragment(),
            FollowFragment()
        )
    }

    private val mTitles = arrayListOf("推荐", "关注")

    override fun setLayoutRes() = R.layout.fragment_community
    override fun isNest() = true

    override fun onFirstVisible() {
        super.onFirstVisible()
        setFragmentsToViewPager(mFragments)
        setTabTitles(mTitles)
    }


}