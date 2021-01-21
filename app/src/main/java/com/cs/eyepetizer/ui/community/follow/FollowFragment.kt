package com.cs.eyepetizer.ui.community.follow

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.common.http.Response
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.repository.Injectors
import com.cs.eyepetizer.ui.community.commend.CommendAdapter
import com.cs.eyepetizer.ui.community.commend.CommendViewModel
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/14
 * @desc 社区-关注
 **/
class FollowFragment : BaseFragment() {

    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            Injectors.getCommunityFollowViewModelFactory()
        ).get(FollowViewModel::class.java)
    }

    private val mAdapter by lazy {
        FollowAdapter(
            this.requireContext(),
            mViewModel.mDataList
        )
    }


    override fun setLayoutRes(): Int = R.layout.fragment_community_follow


    override fun initView() {
        refreshLayout.setOnRefreshListener {
            mViewModel.onRefresh()
        }
        refreshLayout.setOnLoadMoreListener {
            mViewModel.onLoadMore()
        }

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    override fun observer() {

        mViewModel.followResponse.observe(this, Observer {

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
}