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
import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.snackbar.showSnackbar
import com.uchi.resqsync.ui.dialog.UniqueCodeDialog
import com.uchi.resqsync.ui.onboarding.OnboardingActivity
import com.uchi.resqsync.ui.settings.SettingsActivity
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.Utility.generateUniqueCode
import timber.log.Timber


class SettingsFragment : Fragment() {

    private lateinit var signOutButton:LinearLayout
    private lateinit var profileButton:LinearLayout
    private lateinit var uniqueCodeButton:LinearLayout
    private lateinit var currentUserName:TextView
    private lateinit var familyFriendsButton :LinearLayout

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


        currentUserName = view.findViewById(R.id.user_name_text)
        generateUCode()
        signOut()

        familyFriendsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            intent.putExtra("fragment_to_load", "family_friends")
            startActivity(intent)
//            FirebaseUtils().userCircleDetails("family").set(UserCircleModel(listOf(UserModel("ashish","fdfd",""))))
//                .addOnCompleteListener {
//                    showSnackbar("done done")
//                }
        }

    }

    //TODO: real time data base code
//    fun fd(){
//        val x:DatabaseReference = FirebaseDatabase.getInstance().getReference(FirebaseUtils().currentUserId()?:"")
//        val generatedKey=x.push()
//        val userCircleData = UserCircleModel(UserModel("John", "john@example.com",""))
//        x.child("fam").setValue(userCircleData).addOnCompleteListener {
//            showSnackbar("done")
//        }
//    }

    private fun generateUCode() {
        uniqueCodeButton.setOnClickListener {
            val dialog = UniqueCodeDialog()
            dialog.show(parentFragmentManager,"UniqueCodeDialog")
            // TODO: generate this code at time of new login
           // generateAndAddUniqueCode()


        }
    }

    private fun generateAndAddUniqueCode() {
        var generatedCode: String
        do {
            generatedCode = generateUniqueCode()
        } while (isCodeExistsInDatabase(generatedCode))
        FirebaseUtils().uniqueCodeDetails().set(JoiningCode(generatedCode)).addOnCompleteListener {task->
            if (task.isSuccessful){
                Timber.i("User joining code successfully uploaded")
            }
        }
    }

    private fun isCodeExistsInDatabase(code: String): Boolean {
        val query = FirebaseFirestore.getInstance()
            .collection("uniqueCode")
            .whereEqualTo("uniqueCode", code)
        return try {
            val result = query.get().result
            !result.isEmpty
        } catch (e: Exception) {
            Timber.e("Error checking code existence: $e")
            false
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