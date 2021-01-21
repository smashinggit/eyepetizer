package com.cs.eyepetizer.view.tablayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import androidx.viewpager2.widget.ViewPager2
import com.cs.common.utils.dp2px
import com.cs.common.utils.log
import com.cs.common.utils.screenWidth
import kotlinx.android.synthetic.main.item_tab_common.view.*

/**
 *
 * @author  ChenSen
 * @date  2021/1/20
 * @desc  支持水平滑动的 TabLayout,需要配合 ViewPager2 一起使用
 **/
class SlidingTabLayout : HorizontalScrollView {

    // Tab属性
    private var mTabMinWidth = dp2px(40f).toInt()  //TabView的最小宽度
    private var mTabWidth = mTabMinWidth  //Tab的宽度
    private var mTabPaddingLeft = dp2px(0f).toInt()
    private var mTabPaddingTop = dp2px(0f).toInt()
    private var mTabPaddingRight = dp2px(0f).toInt()
    private var mTabPaddingBottom = dp2px(0f).toInt()
    private var mIsTabSpaceEqual = true    //TabView宽度是否相等

    // Title
    private val TEXT_BOLD_NONE = 0
    private val TEXT_BOLD_WHEN_SELECT = 1
    private val TEXT_BOLD_BOTH = 2

    private val mTitleSize = dp2px(6f)
    private val mTitleSelectColor = Color.BLACK
    private val mTitleUnSelectColor = Color.LTGRAY
    private val mTextBold = TEXT_BOLD_NONE  //是否加粗
    private val mTextAllCaps = false


    // 指示器属性
    var mIndicatorWidth = 50
    private var mIndicatorHeight = dp2px(2f).toInt()
    private var mIndicatorMarginLeft = dp2px(0f).toInt()
    private var mIndicatorMarginTop = dp2px(0f).toInt()
    private var mIndicatorMarginRight = dp2px(0f).toInt()
    private var mIndicatorMarginBottom = dp2px(0f).toInt()


    private val mTabContainer by lazy {
        LinearLayout(context).apply {
            val param =
                LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            layoutParams = param
            orientation = HORIZONTAL
        }
    }

    private var mTabCount = 0
    private var mSelectPosition = 0

    private var mScrollX = 0              //当前 ScrollView 的移动距离


    //指示器相关
    private val mIndicatorRect = Rect(0, 0, mIndicatorHeight, mTabWidth) //默认与 TabView宽度一致

    private val mOldIndicatorPosition = Pair(0, 0)  //旧指示器的位置，即当前选中的位置
    private val mNewIndicatorPosition = Pair(0, 0)  //新位置，即点击新的Tab后的位置

    //    private val mIndicatorAnimator = ValueAnimator.ofObject()
    private val mIndicatorPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }


    // ViewPager
    private var mViewPager: ViewPager2? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {


        isFillViewport = true //设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false) //重写onDraw方法,需要调用这个方法来清除flag

        addView(mTabContainer)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                mScrollX = scrollX
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mIndicatorWidth = mTabWidth

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制
        canvas.drawRect(mIndicatorRect, mIndicatorPaint)
    }


    /**
     * 设置标题
     */
    fun setTitles(titles: List<String>) {

        mTabCount = titles.size
        mTabContainer.removeAllViews()

        for (position in titles.indices) {
            mTabContainer.addView(createTabView(position, titles[position]), position)
        }

        updateUi()
    }


    /**
     * 设置 ViewPager2，使其与 TabLayout 进行联动
     */
    fun setUpWithViewPager(viewPager: ViewPager2) {
        this.mViewPager = viewPager
        mViewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                log("onPageSelected $position $mTabWidth  $mIndicatorWidth")
                mSelectPosition = position
                updateUi()
            }
        })
    }

    /**
     * 设置选中的 Tab
     */
    fun setSelected(position: Int) {
        log("setSelected $position  mIndicatorWidth ${mIndicatorWidth}")
        mSelectPosition = position

        if (mViewPager == null) {
            updateUi()
        } else {
            mViewPager?.currentItem = position
        }
    }


    /**
     * 设置指示器的尺寸
     */
    fun setIndicatorSize(width: Int, height: Int) {

        mIndicatorWidth = width
        mIndicatorHeight = height
        log("setIndicatorSize mIndicatorHeight $mIndicatorHeight")
        updateIndicatorPosition()
    }


    /**
     * 设置指示器的 Margin
     */
    fun setIndicatorMargin(left: Int, top: Int, right: Int, bottom: Int) {
        this.mIndicatorMarginLeft = left
        this.mIndicatorMarginTop = top
        this.mIndicatorMarginRight = right
        this.mIndicatorMarginBottom = bottom
        updateIndicatorPosition()
    }


    private fun updateUi() {
        updateTabViewState()  // 通知所有TabView改变状态
        scrollToCenter()     // 使选中的TabView处于屏幕中间
        updateIndicatorPosition()    //更新指示器的位置
    }


    /**
     *  创建新的TabView
     */
    private fun createTabView(position: Int, title: String): TabView {

        return TabView(context).apply {
            minimumWidth = mTabMinWidth
            layoutParams = if (mIsTabSpaceEqual) {
                LinearLayout.LayoutParams(
                    0,
                    LayoutParams.MATCH_PARENT,
                    1f
                ).apply {
                    setPadding(
                        mTabPaddingLeft,
                        mTabPaddingTop,
                        mTabPaddingRight,
                        mTabPaddingBottom
                    )
                }
            } else {
                LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
                ).apply {
                    setPadding(
                        mTabPaddingLeft,
                        mTabPaddingTop,
                        mTabPaddingRight,
                        mTabPaddingBottom
                    )
                }
            }

            tvTitle.apply {
                setTextColor(mTitleUnSelectColor)
                textSize = mTitleSize
                text = title
            }

            setOnClickListener {
                if (mSelectPosition == position) {
                    onReSelect(position)  //重复选择
                } else {
                    setSelected(position)
                }
            }
        }
    }


    /**
     * 选中的TabView移动到居中位置
     */
    private fun scrollToCenter() {
        val selectChild = mTabContainer.getChildAt(mSelectPosition)
        val left = selectChild.left
        val width = selectChild.width

        val offset = left - context.screenWidth() / 2 + width / 2

        smoothScrollTo(offset, 0)
    }


    private fun updateTabViewState() {

        for (i in 0 until mTabCount) {
            (mTabContainer.getChildAt(i) as TabView).apply {
                if (i == mSelectPosition) {  //选中
                    onSelect(i)
                } else {  //未选中
                    onUnSelect(i)
                }
            }
        }
    }


    private fun updateIndicatorPosition() {
        val child = mTabContainer.getChildAt(mSelectPosition)

        if (mIndicatorWidth > child.width) {
            mIndicatorWidth = child.width
        }
        log("updateIndicatorPosition mIndicatorWidth $mIndicatorWidth  a $mIndicatorWidth")

        val margin =
            (child.width - child.paddingLeft - child.paddingRight - mIndicatorWidth) / 2

        mIndicatorRect.set(
            child.left + margin + mIndicatorMarginLeft,
            this@SlidingTabLayout.bottom - mIndicatorHeight - mIndicatorMarginBottom,
            child.right - margin - mIndicatorMarginRight,
            this@SlidingTabLayout.bottom - mIndicatorMarginBottom
        )
        log("mIndicatorRect ${mIndicatorRect.flattenToString()}")
    }

}