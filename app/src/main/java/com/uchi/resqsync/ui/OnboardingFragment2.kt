package com.uchi.resqsync.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.uchi.resqsync.R
import com.uchi.resqsync.utils.PrefConstant

class OnboardingFragment2 : Fragment() {
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

        getStartedButton.setOnClickListener {
            PrefConstant.setOnboardingPref(PrefConstant.firstTimeOpening, activity)
            navRegisterUserFragment()
        }
    }

    private fun navRegisterUserFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer2, RegisterUserFragment())
            .commit()
    }
}