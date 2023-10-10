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
        fragmentView=view

        setUpProfileCard()
        generateUCode()
        signOut()

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