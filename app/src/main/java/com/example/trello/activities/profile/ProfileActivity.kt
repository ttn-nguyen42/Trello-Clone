package com.example.trello.activities.profile


import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.trello.R
import com.example.trello.databinding.ActivityProfileBinding
import com.example.trello.utils.Utils
import com.example.trello.utils.interfaces.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>() {
    override lateinit var binding: ActivityProfileBinding
    override val view: ProfileViewModel by viewModels()

    override fun createBinding() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listenToStateChanges() {
        lifecycleScope.launchWhenStarted {
            view.flow.collect { event: ProfileStates ->
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
                    },
                    finish = {
                        onFinish()
                    }
                )
            }
        }
    }

    override fun buildPage() {
        view.init()
        buildToolbar()
        buildNameField()
        buildNumberField()
        updateButtonStatus()
    }

    private fun onSuccess() {
        val info = view.userInfo!!
        if (info.name != null && info.name.isNotEmpty()) {
            view.name = info.name
            binding.etName.setText(view.name, TextView.BufferType.EDITABLE)
        }
        if (info.mobile != null && info.mobile.isNotEmpty()) {
            view.number = info.mobile
            binding.etNumber.setText(view.number, TextView.BufferType.EDITABLE)
        }
        changeLoadingView(View.INVISIBLE)
    }

    private fun onFailure(message: String) {
        changeLoadingView(View.INVISIBLE)
        Utils.showErrorDialog(this, message, "Board") {
//            finish()
        }
    }

    private fun onLoading() {
        changeLoadingView(View.VISIBLE)
    }

    private fun onInitial() {
        changeLoadingView(View.INVISIBLE)
    }

    private fun onFinish() {
        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    private fun changeLoadingView(view: Int) {
        val loadingView = binding.loadingView.loadingContainer
        if (view == View.VISIBLE) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.INVISIBLE
        }
    }

    private fun buildToolbar() {
        val toolbar = binding.toolbar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.profile)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun buildNameField() {
        val name = binding.etName
        name.addTextChangedListener {
            view.name = it.toString()
            updateButtonStatus()
        }
    }

    private fun buildNumberField() {
        val number = binding.etNumber
        number.addTextChangedListener {
            view.number = it.toString()
            updateButtonStatus()
        }
    }

    private fun updateButtonStatus() {
        val button = binding.buttonSave
        button.isEnabled = view.isButtonEnabled()
        if (!view.isButtonEnabled()) {
            button.setOnClickListener(null)
        } else {
            button.setOnClickListener {
                view.updateUserInfo()
            }
        }
    }
}