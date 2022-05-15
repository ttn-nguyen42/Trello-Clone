package com.example.trello.utils.interfaces

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class BaseActivity<B, V: ViewModel>: AppCompatActivity() {
    abstract var binding: B
    abstract val view: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBinding()
        buildPage()
        listenToStateChanges()
    }

    abstract fun createBinding()
    abstract fun listenToStateChanges()
    abstract fun buildPage()
}