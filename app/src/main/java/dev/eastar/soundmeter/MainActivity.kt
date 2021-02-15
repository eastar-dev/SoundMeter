package dev.eastar.soundmeter

import android.content.Intent
import android.log.Log
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import dev.eastar.soundmeter.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

class MainActivity : AppCompatActivity() {
    private lateinit var bb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate")
        bb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bb.root)
        requestAudioPermission()
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.e(event)
            }
        })
    }

    //sensor request audio
    private fun requestAudioPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                soundMeter()
            }
        }.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    private fun soundMeter() {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                lifecycleScope.launchWhenResumed() {
                    val mSoundMeter = SoundMeter()
                    mSoundMeter.start()
                    val job = async(Dispatchers.Main) {
                        while (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            bb.soundLevel.setLevel(mSoundMeter.getAmplitude())
                            Log.w("running..")
                            yield()
                            //delay(1000)
                        }
                    }
                    job.join()
                    mSoundMeter.stop()
                }
            }
        })
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        //AlertDialog.Builder(this).setTitle("hello").show()
        startActivity(Intent(this , PauseActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.e("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e("onResume")
    }


    //private fun soundMeter() {
    //    lifecycleScope.launch {
    //        lifecycle.withCreated {
    //            Log.e("lifecycle.withCreated")
    //        }
    //        lifecycle.withStarted {
    //            Log.e("lifecycle.withStarted")
    //        }
    //        lifecycle.withResumed {
    //            Log.e("lifecycle.withResumed")
    //        }
    //        lifecycle.whenCreated {
    //            Log.e("lifecycle.whenCreated")
    //        }
    //        lifecycle.whenStarted {
    //            Log.e("lifecycle.whenStarted")
    //
    //        }
    //        lifecycle.whenResumed {
    //            Log.e("lifecycle.whenResumed")
    //
    //        }
    //        lifecycle.whenStateAtLeast(Lifecycle.State.CREATED) {
    //            Log.e("lifecycle.whenStateAtLeast.CREATED")
    //        }
    //        lifecycle.whenStateAtLeast(Lifecycle.State.RESUMED) {
    //            Log.e("lifecycle.whenStateAtLeast.RESUMED")
    //        }
    //        lifecycle.whenStateAtLeast(Lifecycle.State.STARTED) {
    //            Log.e("lifecycle.whenStateAtLeast.STARTED")
    //        }
    //        lifecycle.whenStateAtLeast(Lifecycle.State.DESTROYED) {
    //            Log.e("lifecycle.whenStateAtLeast.DESTROYED")
    //        }
    //        lifecycle.whenStateAtLeast(Lifecycle.State.INITIALIZED) {
    //            Log.e("lifecycle.whenStateAtLeast.INITIALIZED")
    //        }
    //
    //        //lifecycle.withStateAtLeast(Lifecycle.State.DESTROYED) {
    //        //    Log.e("lifecycle.withStateAtLeast.DESTROYED")
    //        //}
    //        //lifecycle.withStateAtLeast(Lifecycle.State.INITIALIZED) {
    //        //    Log.e("lifecycle.withStateAtLeast.INITIALIZED")
    //        //}
    //        lifecycle.withStateAtLeast(Lifecycle.State.STARTED) {
    //            Log.e("lifecycle.withStateAtLeast.STARTED")
    //        }
    //        lifecycle.withStateAtLeast(Lifecycle.State.RESUMED) {
    //            Log.e("lifecycle.withStateAtLeast.RESUMED")
    //        }
    //        lifecycle.withStateAtLeast(Lifecycle.State.CREATED) {
    //            Log.e("lifecycle.withStateAtLeast.CREATED")
    //        }
    //    }
    //    lifecycleScope.launchWhenResumed {
    //        Log.e("lifecycleScope.launchWhenResumed")
    //    }
    //    lifecycleScope.launchWhenCreated {
    //        Log.e("lifecycleScope.launchWhenCreated")
    //    }
    //    lifecycleScope.launchWhenStarted {
    //        Log.e("lifecycleScope.launchWhenStarted")
    //    }
    //    lifecycle.addObserver(object : LifecycleObserver {
    //        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    //        fun onStopped() {
    //            Log.e("Lifecycle.Event.ON_STOP")
    //        }
    //
    //        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    //        fun onCreated(source: LifecycleOwner) {
    //            Log.e("Lifecycle.Event.ON_CREATE", source)
    //        }
    //
    //        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    //        fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
    //            Log.e(event, source)
    //        }
    //    })
    //}
}
