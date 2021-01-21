package com.cs.eyepetizer.view.tablayout

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cs.common.utils.dp2px
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import kotlinx.android.synthetic.main.activity_tabtest.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc
 **/
class DemoActivity : BaseActivity() {

    private val mTitles = listOf(
        "推荐",
        "电脑",
        "美妆",
        "女装",
        "食品",
        "手机",
        "百货",
        "内衣",
        "水果",
        "车品",
        "母婴",
        "家具",
    )

    private val mColors = arrayListOf(
        Color.rgb(153, 50, 204),
        Color.rgb(220, 20, 60),
        Color.rgb(199, 21, 133),
        Color.rgb(221, 160, 221),
        Color.rgb(72, 61, 139),
        Color.rgb(255, 182, 193),
        Color.rgb(0, 0, 128),
        Color.rgb(112, 128, 144),
        Color.rgb(176, 224, 230),
        Color.rgb(64, 224, 208),
        Color.rgb(50, 205, 50),
        Color.rgb(165, 42, 42)
    )


    override fun setLayoutRes(): Int {
        return R.layout.activity_tabtest
    }

    override fun initView() {
        super.initView()

        viewPager.apply {
            adapter = ViewPagerAdapter()
        }


        tabLayout.setTitles(mTitles)
        tabLayout.setUpWithViewPager(viewPager)

        tabLayout.setIndicatorSize(dp2px(30f), dp2px(2f))
    }

    inner class ViewPagerAdapter :
        FragmentStateAdapter(this) {
        override fun getItemCount() = mTitles.size

        override fun createFragment(position: Int): Fragment {
            return DemoFragment.create(mTitles[position], mColors[position])
        }
    }

}

