package com.uchi.resqsync.ui.userdetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.Utility

class UserDetailsFragment : Fragment() {
    private lateinit var submitDetail : MaterialButton
    private lateinit var userFullName: TextInputEditText
    private lateinit var userEmail : TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitDetail = view.findViewById(R.id.submit_details_button)
        userFullName= view.findViewById(R.id.user_full_name)
        userEmail = view.findViewById(R.id.user_email)
        nameInputLayout = view.findViewById(R.id.fullname_input_text_layout)
        emailInputLayout = view.findViewById(R.id.email_input_text_layout)

        submitDetail.setOnClickListener {
            if(!Utility.isEmailValid(userEmail.text.toString())){
                emailInputLayout.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }
            if(userFullName.text.toString().isEmpty()){
                nameInputLayout.error= getString(R.string.name_required)
                return@setOnClickListener
            }
            updateDetails()
        }

    }

    fun updateDetails(){
        FirebaseUtils().currentUserDetails().set(
            UserModel(userFullName.text.toString(),userEmail.text.toString(),FirebaseUtils().currentUserId().toString())
        ).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                PrefConstant.setDetailsProvided(requireContext())
                val intent = Intent(requireContext(), DashBoardActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }


    }

}