package dev.eastar.soundmeter

import android.log.Log
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.eastar.soundmeter.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var bb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bb.root)
        requestAudioPermission()
        //soundMeter()
    }
    //sensor request audio
    private fun requestAudioPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) soundMeter()
        }.launch(android.Manifest.permission.RECORD_AUDIO)
    }
    private fun soundMeter() {
        lifecycleScope.launch {
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