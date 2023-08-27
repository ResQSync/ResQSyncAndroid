package com.uchi.resqsync.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uchi.resqsync.R

class DashBoardActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        loadFragment(LocationMapFragment())
        bottomNavigation = findViewById(R.id.bottomNav)
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    loadFragment(LocationMapFragment())
                    true
                }
                R.id.action_sos -> {
                    loadFragment(EmergencyFragment())
                    true
                }
                R.id.action_account -> {
                    loadFragment(AccountFragment())
                    true
                }
                R.id.action_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_container,fragment)
        transaction.commit()
    }
}