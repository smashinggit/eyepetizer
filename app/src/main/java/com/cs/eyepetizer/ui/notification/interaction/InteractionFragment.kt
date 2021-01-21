package com.cs.eyepetizer.ui.notification.interaction

import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.ui.LoginActivity
import kotlinx.android.synthetic.main.fragment_notification_login_tips.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/15
 * @desc
 **/
class InteractionFragment : BaseFragment() {
    override fun setLayoutRes(): Int = R.layout.fragment_notification_login_tips

    override fun onFirstVisible() {
        super.onFirstVisible()

        tvLogin.setOnClickListener {
            LoginActivity.start(requireContext())
        }
    }
}