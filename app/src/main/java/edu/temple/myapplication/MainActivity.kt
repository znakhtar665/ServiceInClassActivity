package edu.temple.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var timerBinder: TimerService.TimerBinder
    var isBound = false

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder) {
            timerBinder = service as TimerService.TimerBinder
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //Add a bindService()
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        bindService(Intent(this, TimerService.TimerBinder::class.java), serviceConnection, Context.BIND_AUTO_CREATE)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if(timerBinder.isRunning) {
                timerBinder.pause()
            } else {
                timerBinder.start(10)
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder.stop()
        }


    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}