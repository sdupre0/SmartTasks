package com.example.smarttasks.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.smarttasks.R

class IntroActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        val timer = object: CountDownTimer(2000, 1000) {
            override fun onTick(t: Long) { }
            override fun onFinish() {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
        timer.start()
    }
}