package com.cs.eyepetizer.ui

import android.content.Context
import android.content.Intent
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity

/**
 *
 * @author  ChenSen
 * @date  2021/1/15
 * @desc
 **/
class SettingActivity : BaseActivity() {

    override fun setLayoutRes() = R.layout.activity_setting

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}