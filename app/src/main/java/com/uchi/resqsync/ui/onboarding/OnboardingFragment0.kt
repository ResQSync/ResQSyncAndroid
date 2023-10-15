/*
 *  Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.uchi.resqsync.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
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
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
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