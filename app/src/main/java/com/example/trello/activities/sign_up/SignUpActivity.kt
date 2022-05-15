package com.example.trello.activities.sign_up

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.trello.R
import com.example.trello.activities.board.BoardActivity
import com.example.trello.databinding.ActivitySignUpBinding
import com.example.trello.utils.Utils
import com.example.trello.utils.interfaces.BaseActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding, SignUpViewModel>() {
    override lateinit var binding: ActivitySignUpBinding
    override val view: SignUpViewModel by viewModels()

    override fun createBinding() {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listenToStateChanges() {
        lifecycleScope.launchWhenStarted {
            view.flow.collect { event: SignUpStates ->
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
        showLoadingView(View.INVISIBLE)
        val intent: Intent = Intent(this, BoardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onFailure(message: String) {
        showLoadingView(View.INVISIBLE)
        Utils.showErrorDialog(this, message, "Sign up") {
//            finish()
        }
    }

    private fun onLoading() {
        showLoadingView(View.VISIBLE)
    }

    private fun onInitial() {
        showLoadingView(View.INVISIBLE)
    }

    override fun buildPage() {
        buildToolbar()
        buildNameField()
        buildEmailField()
        buildPasswordField()
        buildRetypePasswordField()
        checkSignUpButtonState()
    }

    private fun showLoadingView(view: Int) {
        val loadingView = binding.loadingView.loadingContainer
        if (view == View.VISIBLE) {
            loadingView.visibility = View.VISIBLE
        }
        if (view == View.INVISIBLE) {
            loadingView.visibility = View.INVISIBLE
        }
    }

    private fun buildToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.sign_up)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun buildNameField() {
        val nameLayout: TextInputLayout = binding.etNameLayout
        val name: TextInputEditText = binding.etName
        name.addTextChangedListener {
            view.name = it.toString()
            if (view.name.isEmpty()) {
                nameLayout.error = "This field is required"
            } else {
                nameLayout.error = null
            }
            checkSignUpButtonState()
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
            checkSignUpButtonState()
        }
    }

    private fun buildRetypePasswordField() {
        val retypePasswordLayout: TextInputLayout = binding.etRetypePasswordLayout
        val retypePassword: TextInputEditText = binding.etRetypePassword
        retypePassword.addTextChangedListener {
            view.retype = it.toString()
            if (view.retype != view.password) {
                retypePasswordLayout.error = "Password does not match"
            } else {
                retypePasswordLayout.error = null
            }
            checkSignUpButtonState()
        }
    }

    private fun buildPasswordField() {
        val passwordLayout: TextInputLayout = binding.etPasswordLayout
        val password: TextInputEditText = binding.etPassword
        password.addTextChangedListener {
            view.password = it.toString()
            if (!view.isPasswordValid()) {
                passwordLayout.error = "Password length must be greater than 8"
            } else {
                passwordLayout.error = null
            }
            checkSignUpButtonState()
        }
    }

    private fun checkSignUpButtonState() {
        val signUpButton = binding.buttonSignUp
        signUpButton.isEnabled = !view.isSignUpDisabled()
        if (view.isSignUpDisabled()) {
            signUpButton.setOnClickListener(null)
        } else {
            signUpButton.setOnClickListener {
                view.signUp()
            }
        }
    }
}