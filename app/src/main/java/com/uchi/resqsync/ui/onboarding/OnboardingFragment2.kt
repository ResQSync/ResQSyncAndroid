package com.uchi.resqsync.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.uchi.resqsync.R
import com.uchi.resqsync.ui.phoneAuth.PhoneAuthFragment
import com.uchi.resqsync.utils.PrefConstant

class OnboardingFragment2 : Fragment() {
    private lateinit var navController: NavController
    private lateinit var getStartedButton: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_about2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStartedButton = view.findViewById(R.id.get_started_btn)
        navController = Navigation.findNavController(view)

        getStartedButton.setOnClickListener {
            PrefConstant.setOnboardingPref(PrefConstant.firstTimeOpening, activity)
            navController.navigate(R.id.action_onboardingFragment2_to_phoneAuthFragment)
        }
    }

}