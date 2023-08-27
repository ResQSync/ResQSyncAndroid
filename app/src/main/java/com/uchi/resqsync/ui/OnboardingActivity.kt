package com.uchi.resqsync.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.uchi.resqsync.R

class OnboardingActivity : AppCompatActivity() {
    private lateinit var onboardingImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        onboardingImageButton = findViewById(R.id.onboarding_btn1)
        onboardingImageButton.setOnClickListener {
            navOnboardingScreen()
        }
    }

    private fun navOnboardingScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, OnboardingFragment1()).commit()
    }
}
