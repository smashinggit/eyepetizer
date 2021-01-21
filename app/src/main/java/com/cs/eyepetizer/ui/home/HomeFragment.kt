package com.cs.eyepetizer.ui.home

import android.view.View
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseViewPagerFragment
import com.cs.eyepetizer.ui.home.commend.CommendFragment
import com.cs.eyepetizer.ui.home.daily.DailyFragment
import com.cs.eyepetizer.ui.home.discover.DiscoveryFragment
import kotlinx.android.synthetic.main.layout_page_title.*

/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc :
 **/
class HomeFragment : BaseViewPagerFragment() {

    private val mFragments by lazy {
        return@lazy arrayListOf(
            DiscoveryFragment(),
            CommendFragment(),
            DailyFragment()
        )
    }
    private val mTitles = arrayListOf("发现", "推荐", "今日")

    override fun setLayoutRes() = R.layout.fragment_home

    override fun isNest() = true

    override fun onFirstVisible() {
        super.onFirstVisible()

        ivCalendar.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                toast("该功能暂未开放，敬请期待")
            }
        }

        ivSearch.apply {
            setOnClickListener {
                toast("该功能暂未开放，敬请期待")
            }
        }

        setFragmentsToViewPager(mFragments)
        setTabTitles(mTitles)
        setViewPagerSelectedIndex(1)
    }

}