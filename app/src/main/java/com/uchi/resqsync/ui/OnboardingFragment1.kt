package com.uchi.resqsync.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.uchi.resqsync.R

class OnboardingFragment1 : Fragment() {
    private lateinit var onboardingImageButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_about1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingImageButton = view.findViewById(R.id.onboarding_btn2)
        onboardingImageButton.setOnClickListener {
            navOnboardingScreen()
        }
    }

    private fun navOnboardingScreen() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer1, OnboardingFragment2()).commit()
    }
}