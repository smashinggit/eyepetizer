package com.cs.eyepetizer.ui.notification

import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseViewPagerFragment
import com.cs.eyepetizer.ui.notification.inbox.InboxFragment
import com.cs.eyepetizer.ui.notification.interaction.InteractionFragment
import com.cs.eyepetizer.ui.notification.push.PushFragment

/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc :
 **/
class NotificationFragment : BaseViewPagerFragment() {
    private val mFragments by lazy {
        return@lazy arrayListOf(
            PushFragment(),
            InteractionFragment(),
            InboxFragment()
        )
    }

    private val mTitles = arrayListOf("推送", "互动", "私信")

    override fun setLayoutRes() = R.layout.fragment_notification
    override fun isNest() = true

    override fun onFirstVisible() {
        super.onFirstVisible()

        setFragmentsToViewPager(mFragments)
        setTabTitles(mTitles)
    }


}