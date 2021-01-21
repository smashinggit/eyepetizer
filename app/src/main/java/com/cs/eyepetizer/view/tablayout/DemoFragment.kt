package com.cs.eyepetizer.view.tablayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cs.eyepetizer.R
import kotlinx.android.synthetic.main.fragment_tab_test.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/21
 * @desc
 **/
class DemoFragment(private val text: String, private val backgroundColor: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_test, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flRoot.setBackgroundColor(backgroundColor)
        tvContent.text = text
    }

    companion object {
        fun create(text: String, backgroundColor: Int): DemoFragment {
            return DemoFragment(text, backgroundColor)
        }
    }
}