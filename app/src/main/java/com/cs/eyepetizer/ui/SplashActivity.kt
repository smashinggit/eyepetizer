package com.cs.eyepetizer.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.lifecycle.lifecycleScope
import com.cs.common.utils.startActivity
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * author : Administrator
 * date : 2021/1/7
 * desc : 闪屏页
 **/
class SplashActivity : BaseActivity() {
    private val splashDuration = 3 * 1000L

    private val alphaAnimation by lazy {
        AlphaAnimation(0.5f, 1f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    // Type：
    // Animation.ABSOLUTE：绝对，如果设置这种类型，后面pivotXValue取值就必须是像素点；比如：控件X方向上的中心点，pivotXValue的取值mIvScale.getWidth() / 2f
    // Animation.RELATIVE_TO_SELF：相对于控件自己，设置这种类型，后面pivotXValue取值就会去拿这个取值是乘上控件本身的宽度；比如：控件X方向上的中心点，pivotXValue的取值0.5f
    // Animation.RELATIVE_TO_PARENT：相对于它父容器（这个父容器是指包括这个这个做动画控件的外一层控件）， 原理同上
    private val scaleAnimation by lazy {
        ScaleAnimation(
            1f, 1.05f, 1f, 1.05f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    override fun setLayoutRes() = R.layout.activity_splash


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(3000)
            startActivity<MainActivity>()
            finish()
        }


        ivBg.startAnimation(scaleAnimation)
        ivSlogan.startAnimation(alphaAnimation)
    }


}