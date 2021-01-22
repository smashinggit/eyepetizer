package com.cs.eyepetizer.view.tablayout.listener

/**
 *
 * @author  ChenSen
 * @date  2021/1/21
 * @desc
 **/
interface ITabView {

    fun setTitle(title: String)
    fun setTitleBoundStyle(style: Int)
    fun setSelectTitleColor(color: Int)
    fun setUnSelectTitleColor(color: Int)

    fun onSelect(position: Int)
    fun onReSelect(position: Int)
    fun onUnSelect(position: Int)


    fun setTitleSize(size: Float)
}