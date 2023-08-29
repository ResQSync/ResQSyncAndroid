package com.uchi.resqsync.ui

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.uchi.resqsync.R
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.PrefConstant.getOnboardingSharedPref

class OnboardingActivity : AppCompatActivity() {
    private lateinit var onboardingImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        onboardingImageButton = findViewById(R.id.onboarding_btn1)
        onboardingImageButton.setOnClickListener {
        }

        if (getOnboardingSharedPref(this)) {
            navOnboardingScreen(SigninFragment())
        } else {
            onboardingImageButton.setOnClickListener {
                navOnboardingScreen(OnboardingFragment1())
            }
        }

    }

    private fun navOnboardingScreen(destinationFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, destinationFragment).commit()
    }
}
