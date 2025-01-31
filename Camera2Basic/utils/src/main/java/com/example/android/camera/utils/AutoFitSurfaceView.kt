/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.camera.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import kotlin.math.roundToInt

/**
 * A [SurfaceView] that can be adjusted to a specified aspect ratio and
 * performs padding transformation of input frames.
 */
class AutoFitSurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SurfaceView(context, attrs, defStyle) {

    private var sourceAspectRatio = 0f

    /**
     * Sets the aspect ratio for this view. The size of the view will be
     * measured based on the ratio calculated from the parameters.
     *
     * @param width  Camera resolution horizontal size
     * @param height Camera resolution vertical size
     */
    fun setAspectRatio(width: Int, height: Int) {
        require(width > 0 && height > 0) { "Size cannot be negative" }
        sourceAspectRatio = width.toFloat() / height.toFloat()
        holder.setFixedSize(width, height)
        requestLayout()
    }

    /**
     * Adjust width and height of the SurfaceView to fit the aspect ratio of the source
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val windowAspectRatio = width/height.toFloat()
        if (sourceAspectRatio == 0f) { // custom aspect ratio not set
            setMeasuredDimension(width, height)
            return
        }

        // Performs padding transformation of the camera frames
        val newWidth: Int
        val newHeight: Int

        if (windowAspectRatio > sourceAspectRatio) {
            newHeight = height
            newWidth = (height * sourceAspectRatio).roundToInt()
        } else {
            newWidth = width
            newHeight = (width / sourceAspectRatio).roundToInt()
        }

        Log.d(TAG, "Measured dimensions set: $newWidth x $newHeight")
        setMeasuredDimension(newWidth, newHeight)
    }

    companion object {
        private val TAG = AutoFitSurfaceView::class.java.simpleName
    }
}
