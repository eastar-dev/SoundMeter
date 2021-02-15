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
import android.log.Log
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import kotlin.math.max
import kotlin.math.roundToLong

class SoundLevelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs), AnimatorUpdateListener {

    private var maxRadiusPaint: Paint = Paint().apply {
        color = 0xff666666.toInt()
    }
    private val amplitudePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.design_default_color_secondary) and 0x88ffffff.toInt()
    }
    private val thresholdPaint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, Resources.getSystem().displayMetrics)
    }
    private val amplitudeAnimation: ValueAnimator = ValueAnimator.ofFloat().apply {
        interpolator = AccelerateInterpolator()
        addUpdateListener(this@SoundLevelView)
    }


    @FloatRange(from = 0.0, to = 1.0)
    var threshold: Float = 0.5F

    @FloatRange(from = 0.0, to = 1.0)
    private var amplitude = 0F
    private var maxRadius = with(Resources.getSystem().displayMetrics) { max(heightPixels, widthPixels) / 2F }
    private var cy = Resources.getSystem().displayMetrics.heightPixels / 2.5F
    private var cx = Resources.getSystem().displayMetrics.widthPixels / 2F

    fun setLevel(@FloatRange(from = 0.0, to = 1.0) amplitude: Float) {
        if (amplitude <= this.amplitude)
            return
        Log.e(amplitude)
        with(amplitudeAnimation) {
            setFloatValues(amplitude, 0f)
            duration = (amplitude * 1000L).roundToLong()
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2F
        cy = h / 2.5F
        maxRadius = max(w, h) / 2f
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(cx, cy, maxRadius, maxRadiusPaint)
        canvas.drawCircle(cx, cy, maxRadius * amplitude, amplitudePaint)
        canvas.drawCircle(cx, cy, maxRadius * threshold, thresholdPaint)
        super.draw(canvas)
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        amplitude = animation.animatedValue as Float
        invalidate()
    }

}