package com.example.trello.activities.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.trello.R
import com.example.trello.activities.board.BoardActivity
import com.example.trello.databinding.ActivityBoardBinding
import com.example.trello.databinding.ActivitySignInBinding
import com.example.trello.utils.Utils
import com.example.trello.utils.interfaces.BaseActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>() {
    override lateinit var binding: ActivitySignInBinding
    override val view: SignInViewModel by viewModels()

    override fun createBinding() {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listenToStateChanges() {
        lifecycleScope.launchWhenStarted {
            view.flow.collect { event: SignInStates ->
                event.on(
                    success = {
                        onSuccess()
                    },
                    failure = { message: String ->
                        onFailure(message)
                    },
                    loading = {
                        onLoading()
                    },
                    initial = {
                        onInitial()
                    }
                )
            }
        }
    }

    private fun onSuccess() {
        changeLoadingView(View.INVISIBLE)
        val intent: Intent = Intent(this, BoardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onFailure(message: String) {
        changeLoadingView(View.INVISIBLE)
        Utils.showErrorDialog(this, message, "Sign in") {
//            finish()
        }
    }

    private fun onLoading() {
        changeLoadingView(View.VISIBLE)
    }

    private fun onInitial() {
        changeLoadingView(View.INVISIBLE)
    }

    override fun buildPage() {
        buildToolbar()
        buildEmailField()
        buildPasswordField()
        checkSignInButtonState()
    }

    private fun changeLoadingView(view: Int) {
        val container = binding.loadingView.loadingContainer
        if (view == View.VISIBLE) {
            container.visibility = View.VISIBLE
        }
        if (view == View.INVISIBLE) {
            container.visibility = View.INVISIBLE
        }
    }

    private fun buildEmailField() {
        val emailLayout: TextInputLayout = binding.etEmailLayout
        val email: TextInputEditText = binding.etEmail
        email.addTextChangedListener {
            view.email = it.toString()
            if (view.email.isEmpty()) {
                emailLayout.error = "This field is required"
            } else {
                emailLayout.error = null
            }
            checkSignInButtonState()
        }
    }

    private fun buildPasswordField() {
        val passwordLayout: TextInputLayout = binding.etPasswordLayout
        val password: TextInputEditText = binding.etPassword
        password.addTextChangedListener {
            view.password = it.toString()
            if (view.password.isEmpty()) {
                passwordLayout.error = "This field is required"
            } else {
                passwordLayout.error = null
            }
            checkSignInButtonState()
        }
    }

    private fun checkSignInButtonState() {
        val signInButton = binding.buttonSignIn
        signInButton.isEnabled = !view.isSignInDisabled()
        if (view.isSignInDisabled()) {
            signInButton.setOnClickListener(null)
        } else {
            signInButton.setOnClickListener {
                view.login()
            }
        }
    }

    private fun buildToolbar() {
        val toolbar: Toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.sign_in)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}