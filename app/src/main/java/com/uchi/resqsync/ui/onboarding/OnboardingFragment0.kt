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
import com.uchi.resqsync.ui.phoneAuth.PhoneAuthFragment
import com.uchi.resqsync.utils.PrefConstant

class OnboardingFragment0 : Fragment() {

    private lateinit var onboardingImageButton: ImageButton
    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding0, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        onboardingImageButton = view.findViewById(R.id.onboarding_btn1)
        onboardingImageButton.setOnClickListener {
        }

        if (PrefConstant.getOnboardingSharedPref(requireContext())) {
            navController.navigate(R.id.action_onboardingFragment0_to_phoneAuthFragment)
        } else {
            onboardingImageButton.setOnClickListener {
                    navController.navigate(R.id.action_onboardingFragment0_to_onboardingFragment1)
            }
        }
    }


}