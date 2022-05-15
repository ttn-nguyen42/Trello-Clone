package com.example.trello.activities.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trello.R
import com.example.trello.activities.sign_in.SignInActivity
import com.example.trello.activities.sign_up.SignUpActivity
import com.example.trello.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBinding()
        buildPage()
    }

    private fun createBinding() {
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun buildPage() {
        listenToSignInButton()
        listenToSignUpButton()
    }

    private fun listenToSignInButton() {
        binding.buttonSignIn.setOnClickListener {
            startSignInActivity()
        }
    }

    private fun listenToSignUpButton() {
        binding.buttonSignUp.setOnClickListener {
            startSignUpActivity()
        }
    }

    private fun startSignInActivity() {
        val signInIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInIntent)
//        finish()
    }

    private fun startSignUpActivity() {
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpIntent)
//        finish()
    }
}