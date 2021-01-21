package com.cs.common.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cs.common.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *
 * @author  ChenSen
 * @date  2021/1/12
 * @desc
 **/


fun ImageView.load(url: String, options: RequestOptions.() -> RequestOptions) {
    Glide.with(this.context).load(url).apply(RequestOptions().options()).into(this)
}


fun ImageView.load(
    url: String,
    round: Float = 0f,
    cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL
) {
    if (round == 0f) {
        Glide.with(this.context).load(url).into(this)
    } else {
        val option = RequestOptions.bitmapTransform(
            RoundedCornersTransformation(
                dp2px(round).toInt(), 0,
                cornerType
            )
        ).placeholder(R.drawable.shape_album_loading_bg)

        Glide.with(this.context).load(url).apply(option).into(this)
    }
}
