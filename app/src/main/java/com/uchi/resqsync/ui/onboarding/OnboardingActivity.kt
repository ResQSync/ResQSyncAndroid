package com.uchi.resqsync.ui.onboarding

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.uchi.resqsync.R
import com.uchi.resqsync.ui.phoneAuth.PhoneAuthFragment
import com.uchi.resqsync.utils.PrefConstant.getOnboardingSharedPref

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}
