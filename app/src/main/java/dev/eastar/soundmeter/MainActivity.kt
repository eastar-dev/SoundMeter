package dev.eastar.soundmeter

import android.log.Log
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.eastar.soundmeter.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var job: Job? = null
    private lateinit var bb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bb.root)
        soundMeter()
    }

    private fun soundMeter() {
        job = lifecycleScope.launch {
            SoundMeter().apply {
                start()
                withContext(Dispatchers.Main) {
                    while (true) {
                        Log.e()
                        bb.soundLevel.setLevel(getAmplitude())
                        yield()
                    }
                }
                stop()
            }
        }
    }
}