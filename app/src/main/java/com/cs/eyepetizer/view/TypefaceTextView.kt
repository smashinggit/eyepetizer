package com.cs.eyepetizer.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.cs.eyepetizer.R

/**
 *
 * author : ChenSen
 * date : 2021/1/7
 * desc :
 **/
class TypefaceTextView : AppCompatTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = getTypeface(typefaceType)
            typedArray.recycle()
        }
    }


    /**
     * 根据字体类型，获取自定义字体。
     */
    fun getTypeface(typefaceType: Int?) = when (typefaceType) {
        1 -> Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        2 -> Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        3 -> Typeface.createFromAsset(context.assets, "fonts/Futura-CondensedMedium.ttf")
        4 -> Typeface.createFromAsset(context.assets, "fonts/DIN-Condensed-Bold.ttf")
        5 -> Typeface.createFromAsset(context.assets, "fonts/Lobster-1.4.otf")
        else -> Typeface.DEFAULT
    }
}