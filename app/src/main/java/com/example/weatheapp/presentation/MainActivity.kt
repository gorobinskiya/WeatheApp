package com.example.weatheapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.example.weatheapp.R
import com.example.weatheapp.databinding.ActivityMainBinding
import com.example.weatheapp.presentation.day.DayFragment
import com.example.weatheapp.presentation.dialogs.CitySelectionDialog
import com.example.weatheapp.presentation.week.WeekFragment
import com.example.weatheapp.utils.isConnectedToInternet
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    internal val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        bottomNavigation = binding.bottomNavigation

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DayFragment())
                .commit()
        }



        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dayFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, DayFragment())
                        .commit()
                    true
                }
                R.id.weekFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WeekFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_city -> {
                CitySelectionDialog(this) { selectedCity ->
                    Toast.makeText(this, "Selected city: $selectedCity", Toast.LENGTH_SHORT).show()
                    supportActionBar?.title = selectedCity
                    viewModel.setSelectedCity(selectedCity)
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
