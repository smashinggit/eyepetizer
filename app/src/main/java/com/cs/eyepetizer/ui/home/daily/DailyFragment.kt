package com.cs.eyepetizer.ui.home.daily

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.common.http.Response
import com.cs.common.utils.log
import com.cs.eyepetizer.R
import com.cs.eyepetizer.base.BaseFragment
import com.cs.eyepetizer.repository.Injectors
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/8
 * @desc
 **/

class DailyFragment : BaseFragment() {

    private val mViewModel by lazy {
        ViewModelProvider(
            this,
            Injectors.getHomeDailyViewModelFactory()
        ).get(DailyViewModel::class.java)
    }

    private val mAdapter by lazy {
        DailyAdapter(this.requireContext(), mViewModel.mDataList)
    }

    override fun setLayoutRes(): Int = R.layout.fragment_daily

    override fun initView() {
        refreshLayout.setOnRefreshListener {
            mViewModel.refresh()
        }

        refreshLayout.setOnLoadMoreListener {
            mViewModel.loadMore()
        }

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    override fun observer() {

        mViewModel.dailyResponse.observe(this, Observer {
            when (it.state) {
                Response.State.LOADING -> {

                }

                Response.State.SUCCESS -> {
                    hideLoading()
                    val daily = it.data!!
                    mViewModel.mNextPageUrl = daily.nextPageUrl ?: ""

                    if (mViewModel.mIsLoadMore) {
                        val count = mViewModel.mDataList.count()
                        mViewModel.mDataList.addAll(daily.itemList)
                        mAdapter.notifyItemRangeChanged(count, daily.itemList.size)
                    } else {
                        mViewModel.mDataList.clear()
                        mViewModel.mDataList.addAll(daily.itemList)
                        mAdapter.notifyDataSetChanged()
                    }

                    if (daily.nextPageUrl.isNullOrEmpty()) { //没有更多数据了
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
                    showErrorView {
                        hideErrorView()
                        showLoading()
                        refreshLayout.closeHeaderOrFooter()
                        if (mViewModel.mIsLoadMore) {
                            refreshLayout.autoLoadMore()
                        } else {
                            refreshLayout.autoRefresh()
                        }
                    }
                }
            }
        })

    }

    override fun onFirstVisible() {
        showLoading()
        mViewModel.refresh()
    }


}