package com.jovinz.datingapp.home.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.jovinz.datingapp.R
import com.jovinz.datingapp.common.preferences.UserPrefs
import com.jovinz.datingapp.utils.Constants


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)

        graph.startDestination = if (UserPrefs.getBoolean(Constants.IS_LOGGED_IN))
            R.id.fragment_home
        else
            R.id.fragment_sign_up

        navController.graph = graph
    }
}