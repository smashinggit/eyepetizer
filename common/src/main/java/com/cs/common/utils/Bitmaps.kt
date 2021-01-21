package com.cs.common.utils


object Bitmaps {

    /**
     * 根据屏幕比例计算图片高
     *
     * @param originalWidth   服务器图片原始尺寸：宽
     * @param originalHeight  服务器图片原始尺寸：高
     * @param maxWidth
     * @param maxHeight
     * @return 根据比例缩放后的图片宽高
     */
    fun calculateImageHeight(
        originalWidth: Int,
        originalHeight: Int,
        maxWidth: Int,
        maxHeight: Int
    ): Pair<Int, Int> {

        if (originalWidth <= 0 || originalHeight <= 0) {
            return Pair(originalWidth, originalHeight)
        }
        val width = maxWidth * (originalWidth / originalHeight)
        val height = maxHeight * (originalWidth / originalHeight)

        return Pair(width, height)

    }

}