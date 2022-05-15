package com.example.trello.activities.board

import android.content.Intent
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.trello.R
import com.example.trello.activities.intro.IntroActivity
import com.example.trello.activities.profile.ProfileActivity
import com.example.trello.databinding.ActivityBoardBinding
import com.example.trello.models.shared.UserInfo
import com.example.trello.utils.Utils
import com.example.trello.utils.interfaces.BaseActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardActivity : BaseActivity<ActivityBoardBinding, BoardViewModel>(),
    NavigationView.OnNavigationItemSelectedListener {
    override lateinit var binding: ActivityBoardBinding
    override val view: BoardViewModel by viewModels()

    override fun createBinding() {
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun listenToStateChanges() {
        lifecycleScope.launchWhenStarted {
            view.flow.collect { event: BoardStates ->
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
        val info: UserInfo = view.userInfo!!
        val navigationViewTitle: TextView = binding.navView.findViewById(R.id.drawerUsername)
        if (info.name != null) {
            navigationViewTitle.text = info.name
        } else {
            navigationViewTitle.text = info.email
        }
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

    private fun changeLoadingView(view: Int) {
        val loadingView = binding.loadingView.loadingContainer
        if (view == View.VISIBLE) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.INVISIBLE
        }
    }

    override fun buildPage() {
        view.init()
        buildToolbar()
        listenToDrawerButtons()
    }

    private fun listenToDrawerButtons() {
        val navigationView: NavigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun buildToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_toolbar_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            displayDrawer()
        }
    }

    private fun displayDrawer() {
        val drawer: DrawerLayout = binding.drawerLayout
        drawer.openDrawer(Gravity.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer: DrawerLayout = binding.drawerLayout
        when (item.itemId) {
            R.id.drawerButtonProfile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return false
            }
            R.id.drawerSignOutButton -> {
                view.signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }
        drawer.closeDrawer(Gravity.START)
        return true
    }


}