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

package com.uchi.resqsync.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.uchi.resqsync.R
import com.uchi.resqsync.models.JoiningCode

import com.uchi.resqsync.ui.dialog.UniqueCodeDialog
import com.uchi.resqsync.ui.onboarding.OnboardingActivity
import com.uchi.resqsync.ui.settings.ProfileActivity
import com.uchi.resqsync.ui.settings.SettingsActivity
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.Utility.generateUniqueCode
import org.w3c.dom.Text
import timber.log.Timber


class SettingsFragment : Fragment() {

    private lateinit var signOutButton:LinearLayout
    private lateinit var profileButton:LinearLayout
    private lateinit var uniqueCodeButton:LinearLayout
    private lateinit var currentUsersName:TextView
    private lateinit var familyFriendsButton :LinearLayout
    private lateinit var fragmentView:View
    private lateinit var aboutFragmentButton:LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signOutButton = view.findViewById(R.id.action_sign_out)
        profileButton = view.findViewById(R.id.action_profile)
        uniqueCodeButton = view.findViewById(R.id.action_unique_code)
        familyFriendsButton = view.findViewById(R.id.action_family_friends)
        aboutFragmentButton = view.findViewById(R.id.action_about)
        fragmentView=view

        setUpProfileCard()
        generateUCode()
        signOut()

        aboutFragmentButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            intent.putExtra("fragment_to_load", "about_fragment")
            startActivity(intent)
        }

        familyFriendsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            intent.putExtra("fragment_to_load", "family_friends")
            startActivity(intent)
        }

    }

    private fun setUpProfileCard() {
        currentUsersName = fragmentView.findViewById(R.id.user_name_display)
        val user =PrefConstant.getUserDetails(requireContext())
        currentUsersName.text = user.name
        profileButton.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun generateUCode() {
        uniqueCodeButton.setOnClickListener {
            val dialog = UniqueCodeDialog.newInstance(null,true,true)
            dialog.show(parentFragmentManager,"UniqueCodeDialog")
        }
    }

    private fun signOut() {
        signOutButton.setOnClickListener {
            try {
                FirebaseUtils().logout()
                val intent = Intent(requireContext(), OnboardingActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }catch (e:Exception){
                Timber.e("Error signing out")
            }
        }
    }

}