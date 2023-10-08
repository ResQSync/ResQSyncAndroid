package com.uchi.resqsync.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.uchi.resqsync.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val fragmentToLoad = intent.getStringExtra("fragment_to_load")
        if (fragmentToLoad == "family_friends") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings_nav_layout, FamilyFriendsFragment())
                .commit()
        } else if (fragmentToLoad == "other_fragment") {
            // Load another fragment if needed
        }

    }
}