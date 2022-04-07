package com.mabdigital.core.base.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mabdigital.core.R
import com.mabdigital.core.databinding.ActivityBaseBinding

abstract class BaseActivity : AppCompatActivity() {
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
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            navController?.setGraph(it)
        }
    }
    abstract fun navigationGraph(): Int?
    abstract fun doOnCreate(savedInstanceState: Bundle?)
}