/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.eastar.soundmeter

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.roundToLong

class SoundLevelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs), AnimatorUpdateListener {


    private val mBackgroundPaint: Paint by lazy {
        Paint().apply {
            color = Color.TRANSPARENT
        }
    }
    private val mVolPaint: Paint by lazy {
        Paint().apply {
            color = context.getColor(R.color.design_default_color_secondary)
            alpha = 0xcc
        }
    }

    private val mLinePaint: Paint by lazy {
        Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, Resources.getSystem().displayMetrics)
        }
    }

    private var mThreshold: Float = 200F
    private val decrease: ValueAnimator by lazy {
        ValueAnimator.ofFloat().apply {
            interpolator = AccelerateInterpolator()
            addUpdateListener(this@SoundLevelView)
        }
    }

    private var cx = 0
    private var cy = 0
    private var mVol = 0f
    private var unit = 0f

    //may be 0~100
    fun setThreshold(threshold: Float) {
        mThreshold = threshold * 100 * unit
    }

    //may be 0~100
    fun setLevel(amplitude: Float) {
        val currentVol = amplitude * unit
        if (currentVol <= mVol) return
        with(decrease) {
            setFloatValues(currentVol, 0f)
            duration = (amplitude * 20L).roundToLong()
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2
        cy = (h / 2.5f).toInt()
        unit = max(w, h) / 2f / 100f
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(mBackgroundPaint)
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mVol, mVolPaint)
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mThreshold, mLinePaint)
        super.draw(canvas)
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mVol = animation.animatedValue as Float
        invalidate()
    }

}