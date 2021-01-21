package com.cs.eyepetizer.ui.community.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseActivity
import com.cs.eyepetizer.repository.bean.CommunityRecommend.Item
import com.cs.eyepetizer.ui.common.AutoPlayPageChangeListener
import com.cs.eyepetizer.utils.IntentDataHolder
import com.shuyu.gsyvideoplayer.GSYVideoManager
import kotlinx.android.synthetic.main.activity_community_detail.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc 社区- 详情页面
 **/
class CommunityDetailActivity : BaseActivity() {

    private val mViewModel by lazy {
        ViewModelProvider(this).get(CommunityDetailViewModel::class.java)
    }

    private val mAdapter by lazy {
        CommunityDetailAdapter(this, mViewModel.dataList)
    }

    override fun setLayoutRes() = R.layout.activity_community_detail

    override fun onCreate(savedInstanceState: Bundle?) {

        val recommendList = IntentDataHolder.getData<List<Item>>(EXTRA_RECOMMEND_ITEM_LIST_JSON)
        val currentItem = IntentDataHolder.getData<Item>(EXTRA_RECOMMEND_ITEM_JSON)
        if (recommendList.isNullOrEmpty() || currentItem == null) {
            toast(resources.getString(R.string.jump_page_unknown_error))
            finish()
            return
        } else {
            mViewModel.dataList = recommendList
            mViewModel.itemPosition = recommendList.indexOf(currentItem)
        }

        super.onCreate(savedInstanceState)
        setStatusBarBackground(R.color.black)
    }

    override fun initView() {
        viewPager.adapter = mAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.offscreenPageLimit = 1
        viewPager.registerOnPageChangeCallback(AutoPlayPageChangeListener(viewPager, mViewModel.itemPosition, R.id.videoPlayer))
        viewPager.setCurrentItem(mViewModel.itemPosition, false)
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        GSYVideoManager.releaseAllVideos()
        super.onDestroy()
    }


    companion object {
        const val TAG = "UgcDetailActivity"
        const val EXTRA_RECOMMEND_ITEM_LIST_JSON = "recommend_item_list"
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommend_item"

        fun start(
            context: Context,
            dataList: List<Item>,
            currentItem: Item
        ) {
            IntentDataHolder.setData(EXTRA_RECOMMEND_ITEM_LIST_JSON, dataList)
            IntentDataHolder.setData(EXTRA_RECOMMEND_ITEM_JSON, currentItem)
            val intent = Intent(context, CommunityDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}