package com.uchi.resqsync.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uchi.resqsync.R

class OnboardingFragment1 : Fragment() {
    private lateinit var onboardingImageButton: ImageButton
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_about1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController  = Navigation.findNavController(view)
        onboardingImageButton = view.findViewById(R.id.onboarding_btn2)
        onboardingImageButton.setOnClickListener {
            navController.navigate(R.id.action_onboardingFragment1_to_onboardingFragment2)
        }
    }

}