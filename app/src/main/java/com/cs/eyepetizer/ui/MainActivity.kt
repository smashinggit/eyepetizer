package com.cs.eyepetizer.ui

import android.Manifest
import android.os.Bundle
import com.cs.common.utils.log
import com.cs.common.utils.setOnClickListener
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import com.cs.eyepetizer.ui.community.CommunityFragment
import com.cs.eyepetizer.ui.home.HomeFragment
import com.cs.eyepetizer.ui.mine.MineFragment
import com.cs.eyepetizer.ui.notification.NotificationFragment
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.layout_bottom_navigation.*

class MainActivity : BaseActivity() {
    private val homeFragment by lazy {
        HomeFragment()
    }
    private val communityFragment by lazy {
        CommunityFragment()
    }
    private val notificationFragment by lazy {
        NotificationFragment()
    }
    private val mineFragment by lazy {
        MineFragment()
    }

    private val mFragments =
        arrayOf(homeFragment, communityFragment, notificationFragment, mineFragment)

    override fun setLayoutRes() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOnClickListener(rlHome, rlCommunity, rlRelease, rlNotification, rlMine) {
            when (this) {
                rlHome -> {
                    if (rlHome.isSelected) return@setOnClickListener
                    refreshBottomNavigation(0)
                    hideAndShowFragment(0)
                }
                rlCommunity -> {
                    if (rlCommunity.isSelected) return@setOnClickListener
                    refreshBottomNavigation(1)
                    hideAndShowFragment(1)
                }
                rlRelease -> {

                }
                rlNotification -> {
                    if (rlNotification.isSelected) return@setOnClickListener
                    hideAndShowFragment(2)
                    refreshBottomNavigation(2)
                }
                rlMine -> {
                    if (rlMine.isSelected) return@setOnClickListener
                    hideAndShowFragment(3)
                    refreshBottomNavigation(3)
                }
            }
        }

        rlHome.performClick()

        requestPermissions()
    }


    fun requestPermissions() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "开眼需要以下权限",
                    "确定",
                    "取消"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "开眼需要以下权限，请前往设置中心打开",
                    "设置",
                    "取消"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                grantedList.forEach {
                    log("grantedList $it")
                }

                deniedList.forEach {
                    log("deniedList $it")
                }
            }
    }


    private fun refreshBottomNavigation(index: Int) {
        clearAllSelected()
        when (index) {
            0 -> {
                ivHome.isSelected = true
                tvHome.isSelected = true

            }
            1 -> {
                ivCommunity.isSelected = true
                tvCommunity.isSelected = true
            }
            2 -> {
                ivNotification.isSelected = true
                tvNotification.isSelected = true
            }
            3 -> {
                ivMine.isSelected = true
                tvMine.isSelected = true
            }
        }
    }

    private fun clearAllSelected() {
        ivHome.isSelected = false
        tvHome.isSelected = false
        ivCommunity.isSelected = false
        tvCommunity.isSelected = false
        ivNotification.isSelected = false
        tvNotification.isSelected = false
        ivMine.isSelected = false
        tvMine.isSelected = false

    }

    private fun hideAndShowFragment(index: Int) {
        supportFragmentManager.beginTransaction().apply {
            mFragments.forEach {
                if (it.isVisible) {
                    hide(it)
                }
            }
            when (index) {
                0 -> {
                    if (!homeFragment.isAdded) {
                        add(R.id.container, homeFragment)
                    } else {
                        show(homeFragment)
                    }
                }
                1 -> {
                    if (!communityFragment.isAdded) {
                        add(R.id.container, communityFragment)
                    } else {
                        show(communityFragment)
                    }
                }
                2 -> {
                    if (!notificationFragment.isAdded) {
                        add(R.id.container, notificationFragment)
                    } else {
                        show(notificationFragment)
                    }
                }
                3 -> {
                    if (!mineFragment.isAdded) {
                        add(R.id.container, mineFragment)
                    } else {
                        show(mineFragment)
                    }
                }
            }
        }.commitAllowingStateLoss()
    }


    var mLastBackTime = 0L
    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (now - mLastBackTime < 2000) {
            super.onBackPressed()
        } else {
            mLastBackTime = now
            toast(getString(R.string.press_again_to_exit))
        }
    }

}