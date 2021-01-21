package com.cs.eyepetizer.ui.mine

import androidx.multidex.BuildConfig
import com.cs.common.utils.Versions
import com.cs.common.utils.setOnClickListener
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.ui.LoginActivity
import com.cs.eyepetizer.ui.SettingActivity
import com.cs.eyepetizer.ui.WebViewActivity
import com.cs.eyepetizer.ui.WebViewActivity.Companion.MODE_SONIC_WITH_OFFLINE_CACHE
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc :
 **/
class MineFragment : BaseFragment() {


    override fun setLayoutRes() = R.layout.fragment_mine

    override fun onFirstVisible() {
        super.onFirstVisible()

        tvVersionNumber.text =
            String.format("Version %s", Versions.appVersionName(requireContext()))
        setOnClickListener(
            ivMore,
            ivAvatar,
            tvLoginTips,
            tvFavorites,
            tvCache,
            tvFollow,
            tvWatchRecord,
            tvNotificationToggle,
            tvMyBadge,
            tvVersionNumber,
            rootView,
            llScrollViewContent
        ) {
            when (this) {
                ivMore -> SettingActivity.start(requireContext())

                ivAvatar, tvLoginTips, tvFavorites, tvCache, tvFollow, tvWatchRecord, tvNotificationToggle, tvMyBadge -> {
                    LoginActivity.start(requireContext())
                }
                tvVersionNumber -> {
                    WebViewActivity.start(
                        requireContext(),
                        WebViewActivity.DEFAULT_TITLE,
                        WebViewActivity.DEFAULT_URL,
                        true,
                        false,
                        MODE_SONIC_WITH_OFFLINE_CACHE
                    )
                }

                tvFeedback -> {
                    WebViewActivity.start(
                        requireContext(),
                        WebViewActivity.DEFAULT_TITLE,
                        WebViewActivity.DEFAULT_URL,
                        true,
                        false,
                        MODE_SONIC_WITH_OFFLINE_CACHE
                    )
                }

                this@MineFragment.rootView, llScrollViewContent -> {
//                    MobclickAgent.onEvent(activity, Const.Mobclick.EVENT4)
//                    AboutActivity.start(activity)
                }

                else -> {

                }
            }
        }

        tvVersionNumber.setOnLongClickListener {
            toast(String.format("build_type %s", BuildConfig.BUILD_TYPE))
            true
        }
    }

}