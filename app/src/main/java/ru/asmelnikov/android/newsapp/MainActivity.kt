package ru.asmelnikov.android.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_NewsApp)
        setContentView(R.layout.activity_main)

        bottom_nav_menu.setupWithNavController(
            navController = nav_host_fragment.findNavController()
        )
    }
}
