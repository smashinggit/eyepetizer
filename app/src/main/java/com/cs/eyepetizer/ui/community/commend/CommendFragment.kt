package com.cs.eyepetizer.ui.community.commend

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.common.http.Response
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.repository.Injectors
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc  社区-推荐
 **/
class CommendFragment : BaseFragment() {


    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            Injectors.getCommunityCommendViewModelFactory()
        ).get(CommendViewModel::class.java)
    }

    private val mAdapter by lazy {
        CommendAdapter(
            this.requireContext(),
            mViewModel.mDataList
        )
    }


    override fun setLayoutRes(): Int = R.layout.fragment_community_commend


    override fun initView() {
        refreshLayout.setOnRefreshListener {
            mViewModel.onRefresh()
        }
        refreshLayout.setOnLoadMoreListener {
            mViewModel.onLoadMore()
        }

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(ItemDecoration())
    }

    override fun observer() {

        mViewModel.communityRecommend.observe(this, Observer {

            when (it.state) {
                Response.State.LOADING -> {
                }

                Response.State.SUCCESS -> {
                    hideLoading()

                    val commend = it.data!!
                    mViewModel.mNextPageUrl = commend.nextPageUrl ?: ""

                    if (mViewModel.mIsLoadMore) {
                        val count = mViewModel.mDataList.size
                        mViewModel.mDataList.addAll(commend.itemList)
                        mAdapter.notifyItemRangeChanged(count, commend.itemList.size)
                    } else {
                        mViewModel.mDataList.clear()
                        mViewModel.mDataList.addAll(commend.itemList)
                        mAdapter.notifyDataSetChanged()
                    }

                    if (commend.nextPageUrl.isNullOrEmpty()) { //没有更多数据了
                        refreshLayout.setNoMoreData(true)
                    }

                    if (mViewModel.mIsLoadMore) {
                        refreshLayout.finishLoadMore()
                    } else {
                        refreshLayout.finishRefresh()
                    }
                }

                Response.State.ERROR -> {
                    hideLoading()
                    toast(it.message ?: "")
                    refreshLayout.closeHeaderOrFooter()
                    showErrorView {
                        hideErrorView()
                        showLoading()
                        mViewModel.onRefresh()
                    }
                }
            }
        })
    }

    override fun onFirstVisible() {
        showLoading()
        mViewModel.onRefresh()
    }


    /**
     * 社区整个垂直列表的间隙
     */
    inner class ItemDecoration() : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            outRect.top = bothSideSpace

            when (spanIndex) {
                0 -> {
                    outRect.left = bothSideSpace
                    outRect.right = middleSpace
                }
                else -> {
                    outRect.left = middleSpace
                    outRect.right = bothSideSpace
                }
            }
        }
    }

    //列表左or右间距
    val bothSideSpace by lazy {
        requireContext().resources.getDimension(R.dimen.listSpaceSize).toInt()
    }

    //列表中间内间距，左or右。
    val middleSpace = com.cs.common.utils.dp2px(3f).toInt()

}