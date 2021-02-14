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
import kotlin.math.max

class SoundMeter {
    private var mRecorder: MediaRecorder? = null
    private var mEMA = 0.0
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
        release()
    }

    //return  may be 0~100%
    fun getAmplitude(): Float {
        mRecorder ?: return 0f

        val maxAmplitude = mRecorder!!.maxAmplitude
        return max(0f, maxAmplitude - 2000f) / 28000f * 100f
    }

    fun getAmplitudeEMA(): Double {
        var amp = 0.0
        if (mRecorder != null)
            amp = mRecorder!!.maxAmplitude / 2700.0
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA
        return mEMA
    }

    companion object {
        private const val EMA_FILTER = 0.6
    }
}