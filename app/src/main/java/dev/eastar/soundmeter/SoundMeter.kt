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

import android.media.MediaRecorder
import androidx.annotation.FloatRange
import kotlin.math.log10
import kotlin.math.max

class SoundMeter {
    private var mRecorder: MediaRecorder? = null
    fun start() = with(MediaRecorder()) {
        mRecorder = this
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        setOutputFile("/dev/null")
        prepare()
        start()
    }


    fun stop() = mRecorder?.run {
        stop()
        reset()
        release()
    }

    //return about 0~100
    @FloatRange(from = 0.0, to = 1.0)
    fun getMaxAmplitudeLevel(): Float {
        mRecorder ?: return 0F

        //0~32767
        val maxAmplitude = mRecorder!!.maxAmplitude

        //2000.0이하는 무시 노이즈영역
        return max(0F, maxAmplitude - 2000F) / (32767F - 2000F)
    }


    /**https://stackoverflow.com/questions/10655703/what-does-androids-getmaxamplitude-function-for-the-mediarecorder-actually-gi*/
    val REFERENCE = 0.1

    @Suppress("FunctionName")
    fun getMax_dB(): Double {
        mRecorder ?: return 0.0

        //0~32767
        val maxAmplitude = mRecorder!!.maxAmplitude.toDouble()
        return max(0.0, 20 * log10(maxAmplitude / REFERENCE))
    }
}