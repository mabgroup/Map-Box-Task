package com.mabdigital.core.base.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.mabdigital.core.R
import com.mabdigital.core.base.model.NotificationModel
import com.mabdigital.core.base.notification.NOTIFICATION_DATA
import com.mabdigital.core.databinding.ActivityBaseBinding

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var navHostFragment: NavHostFragment
    private lateinit var baseBinding :ActivityBaseBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)
        initializeNavigationGraph()
        doOnCreate(savedInstanceState)
    }

    private fun initializeNavigationGraph() {
        navigationGraph()?.let {
            navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            navController?.setGraph(it)
        }
    }

    fun showSnackBarMessage(
        string: String? = null,
        stringResource: Int? = null
    ) {

        val snackBar: Snackbar = if (stringResource == null) {
            val message = string ?: ""
            Snackbar.make(
                baseBinding.root,
                message,
                Snackbar.LENGTH_LONG
            )
        } else
            Snackbar.make(
                baseBinding.root,
                stringResource,
                Snackbar.LENGTH_LONG
            )
        ViewCompat.setLayoutDirection(snackBar.view, ViewCompat.LAYOUT_DIRECTION_LTR)
        snackBar.show()
    }
    abstract fun navigationGraph(): Int?
    abstract fun doOnCreate(savedInstanceState: Bundle?)
}