package ru.asmelnikov.android.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.asmelnikov.android.newsapp.databinding.ActivityMainBinding
import ru.asmelnikov.android.newsapp.ui.favorite.FavoriteFragment
import ru.asmelnikov.android.newsapp.ui.main.MainFragment
import ru.asmelnikov.android.newsapp.ui.search.SearchFragment

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
