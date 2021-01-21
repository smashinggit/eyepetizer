package com.cs.eyepetizer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.cs.common.utils.*
import com.cs.eyepetizer.Constant
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import com.cs.eyepetizer.utils.setDrawable
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_title.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/12
 * @desc
 **/
class LoginActivity : BaseActivity() {

    override fun setLayoutRes(): Int = R.layout.activity_login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarBackground(R.color.black)

        initTitleBar()
        initListener()
    }


    private fun initTitleBar() {
        titleBar.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.actionBarSizeSecondary)
        titleBar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        val padding = dp2px(9f)
        ivBack.setPadding(padding, padding, padding, padding)
        ivBack.setImageResource(R.drawable.ic_close_white_24dp)
        tvRightText.visible()
        tvRightText.text = resources.getString(R.string.forgot_password)
        tvRightText.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.white))
        tvRightText.textSize = 12f

        etPhoneNumberOrEmail.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_person_white_18dp
            ), 18f, 18f, 0
        )
        etPassWord.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_password_white_lock_18dp
            ), 18f, 18f, 0
        )
        divider.gone()
    }

    private fun initListener() {
        setOnClickListener(
            ivBack,
            tvRightText,
            tvUserLogin,
            tvUserRegister,
            tvAuthorLogin,
            tvUserAgreement,
            tvUserLogin,
            ivWechat,
            ivSina,
            ivQQ
        ) {
            when (this) {
                ivBack -> {
                    onBackPressed()
                }
                tvRightText -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Constant.Url.FORGET_PASSWORD,
                        false,
                        false
                    )
                }
                tvUserRegister -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Constant.Url.AUTHOR_REGISTER,
                        false,
                        false
                    )
                }
                tvAuthorLogin -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Constant.Url.AUTHOR_LOGIN,
                        false,
                        false
                    )
                }
                tvUserAgreement -> {
                    WebViewActivity.start(
                        this@LoginActivity,
                        WebViewActivity.DEFAULT_TITLE,
                        Constant.Url.USER_AGREEMENT,
                        false,
                        false
                    )
                }
                tvUserLogin, ivWechat, ivSina, ivQQ -> {
                    toast(resources.getString(R.string.currently_not_supported))

//                    MobclickAgent.onEvent(this@LoginActivity,Constant.Mobclick.EVENT_LOGIN)

                    val map = HashMap<String, String>().apply {
                        put("userName", etPhoneNumberOrEmail.text.toString())
                        put("password", etPassWord.text.toString())
                    }

                    MobclickAgent.onEvent(this@LoginActivity, Constant.Mobclick.EVENT_LOGIN, map)
                }
                else -> {
                }
            }
        }
    }


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}