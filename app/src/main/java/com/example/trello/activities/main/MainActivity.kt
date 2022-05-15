package com.example.trello.activities.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.viewModels
import com.example.trello.R
import com.example.trello.activities.board.BoardActivity
import com.example.trello.activities.intro.IntroActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    private val kSkipToIntroTime: Long = 2000
    private val kInterval: Long = 1000

    private val view: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callTimer()
    }

    private fun skipToIntroScreen() {
        val introIntent: Intent = Intent(this, IntroActivity::class.java)
        startActivity(introIntent)
        finish()
    }

    private fun skipToBoardScreen() {
        val boardIntent: Intent = Intent(this, BoardActivity::class.java)
        startActivity(boardIntent)
        finish()
    }

    private fun redirect() {
        Log.d("SPLASH", view.isLoggedIn().toString())
        if (view.isLoggedIn()) {
            skipToBoardScreen()
        } else {
            skipToIntroScreen()
        }
    }

    private fun callTimer() {
        discardTimer()
        timer = object : CountDownTimer(kSkipToIntroTime, kInterval) {
            override fun onTick(timerValue: Long) {}
            override fun onFinish() {
                discardTimer()
                redirect()
            }
        }
        timer?.start()
    }

    private fun discardTimer() {
        timer?.cancel();
        timer = null
    }

}