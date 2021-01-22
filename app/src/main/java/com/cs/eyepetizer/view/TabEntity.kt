package com.cs.eyepetizer.view

import com.flyco.tablayout.listener.CustomTabEntity

/**
 *
 * @author  ChenSen
 * @date  2021/1/22
 * @desc
 **/
class TabEntity(
    private var title: String,
    private var selectedIcon: Int = 0,
    private var unSelectedIcon: Int = 0
) :
    CustomTabEntity {
    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabTitle(): String {
        return title
    }


}