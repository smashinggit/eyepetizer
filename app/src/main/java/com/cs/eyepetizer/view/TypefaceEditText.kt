package com.cs.eyepetizer.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.cs.eyepetizer.R

/**
 * 带有自定义字体EditText
 */
class TypefaceEditText : AppCompatEditText {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("CustomViewStyleable")
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
    private fun getTypeface(typefaceType: Int?) = when (typefaceType) {
        1 -> Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
        2 -> Typeface.createFromAsset(context.assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
        3 -> Typeface.createFromAsset(context.assets, "fonts/Futura-CondensedMedium.ttf")
        4 -> Typeface.createFromAsset(context.assets, "fonts/DIN-Condensed-Bold.ttf")
        5 -> Typeface.createFromAsset(context.assets, "fonts/Lobster-1.4.otf")
        else -> Typeface.DEFAULT
    }
}