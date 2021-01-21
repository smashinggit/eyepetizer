package com.cs.eyepetizer.ui.home.commend

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.common.http.Response
import com.cs.common.utils.toast
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.repository.Injectors
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/8
 * @desc  首页- 推荐
 **/

class CommendFragment : BaseFragment() {
    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            Injectors.getHomeRecommendViewModelFactory()
        ).get(CommendViewModel::class.java)
    }

    private val mAdapter by lazy {
        CommendAdapter(this.requireContext(), mViewModel.mDataList)
    }


    override fun setLayoutRes(): Int = R.layout.fragment_commend

    override fun initView() {

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        refreshLayout.setOnRefreshListener {
            mViewModel.onRefresh()
        }

        refreshLayout.setOnLoadMoreListener {
            mViewModel.onLoadMore()
        }
    }

    override fun onFirstVisible() {
        showLoading()
        mViewModel.onRefresh()
    }

    override fun observer() {

        mViewModel.commendPageLiveData2.observe(this, Observer {

            when (it.state) {
                Response.State.LOADING -> {
                }

                Response.State.SUCCESS -> {
                    hideLoading()

                    val commendPage = it.data!!
                    mViewModel.mNextPageUrl = commendPage.nextPageUrl

                    if (mViewModel.mIsLoadMore) {
                        val count = mViewModel.mDataList.size
                        mViewModel.mDataList.addAll(commendPage.itemList)
                        mAdapter.notifyItemRangeChanged(count, commendPage.itemList.size)
                    } else {
                        mViewModel.mDataList.clear()
                        mViewModel.mDataList.addAll(commendPage.itemList)
                        mAdapter.notifyDataSetChanged()
                    }

                    if (commendPage.nextPageUrl.isNullOrEmpty()) { //没有更多数据了
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

}