package com.cs.eyepetizer.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cs.eyepetizer.R
import com.cs.eyepetizer.view.TabEntity
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener

/**
 *
 * @author  ChenSen
 * @date  2021/1/8
 * @desc
 **/
abstract class BaseViewPagerFragment : BaseFragment() {

    protected var mViewPager: ViewPager2? = null
    protected val mAdapter by lazy {
        FragmentPagerAdapter(if (isNest()) childFragmentManager else fragmentManager!!, lifecycle)
    }
    protected var mOffscreenPageLimit = 1

    //    private var mTabLayout: TabLayout? = null   //这是原生的TabLayout,一般都使用自定义的TabLayout
    private var mTabLayout: CommonTabLayout? = null


    override fun onFirstVisible() {
        super.onFirstVisible()

        mViewPager = mRootView?.findViewById(R.id.viewPager)
        mTabLayout = mRootView?.findViewById(R.id.tabLayout)

        mViewPager?.apply {
            adapter = mAdapter
            offscreenPageLimit = setOffscreenPageLimit()
        }
    }


    fun setFragmentsToViewPager(fragment: List<Fragment>) {
        mAdapter.updateFragments(fragment)
    }

    fun setTabTitles(titles: List<String>) {
        if (mTabLayout != null && mViewPager != null) {


            val tabEntities = arrayListOf<CustomTabEntity>()
            titles.forEach {
                tabEntities.add(TabEntity(it))
            }

            mTabLayout?.apply {
                setTabData(tabEntities)
                setOnTabSelectListener(object : OnTabSelectListener {
                    override fun onTabSelect(position: Int) {
                        mViewPager?.currentItem = position
                    }

                    override fun onTabReselect(position: Int) {
                    }
                })
            }


            mViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mTabLayout?.currentTab = position
                }
            })


            //原生写法
//            TabLayoutMediator(
//                mTabLayout!!,
//                mViewPager!!,
//                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
//                    tab.text = titles[position]
//                }).apply {
//                attach()
//            }
        }
    }

    fun setViewPagerSelectedIndex(index: Int) {
        if (index > 0 && index < mAdapter.itemCount) {
            mViewPager?.currentItem = index
        }
    }

    fun setOffscreenPageLimit(): Int = mOffscreenPageLimit

    /**
     * 是否 Fragment 嵌套 Fragment
     */
    open fun isNest(): Boolean = false
}

class FragmentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var mFragments: List<Fragment> = emptyList()
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = mFragments.size

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

    fun updateFragments(fragments: List<Fragment>) {
        this.mFragments = fragments
        notifyDataSetChanged()
    }

}