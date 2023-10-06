package com.uchi.resqsync.ui.email

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.uchi.resqsync.R
import com.uchi.resqsync.api.BackendService
import com.uchi.resqsync.api.RegistrationRequest
import com.uchi.resqsync.api.RegistrationResponse
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.utils.Utility
import com.uchi.resqsync.utils.Utility.makeLinks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class RegisterUserFragment : Fragment() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var cnfPasswordInput: TextInputEditText
    private lateinit var signUpBtn: MaterialButton
    private lateinit var existingUserText: TextView
    private lateinit var tcCheckBox: CheckBox
    private lateinit var email:String
    private lateinit var password: String

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailInput = view.findViewById(R.id.user_email)
        passwordInput = view.findViewById(R.id.user_password)
        cnfPasswordInput = view.findViewById(R.id.cnf_user_password)
        signUpBtn = view.findViewById(R.id.signup_button)
        existingUserText = view.findViewById(R.id.existing_user_text)
        tcCheckBox = view.findViewById(R.id.terms_condition_checkbox)

        existingUserText.highlightColor = Color.TRANSPARENT;
        tcCheckBox.highlightColor = Color.TRANSPARENT;
        signUpBtn.isEnabled = false

        watchInputFieldsAndCheckBox()
        privacyPolicyWeb()
        signUpBtn.setOnClickListener {
            fireBaseRegisterUser()
        }
        existingUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_register_user, container, false)
    }

    private fun existingUser() {
        existingUserText.makeLinks(
            Pair("Signin", View.OnClickListener {
                Timber.w("Signin text pressed")
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, SigninFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            })
        )
    }

    // TODO redirect the user to the web on the link click
    private fun privacyPolicyWeb() {
        tcCheckBox.makeLinks(
            Pair("Privacy", View.OnClickListener {
                Timber.w("Privacy text pressed")
            }),
            Pair("Terms of Use", View.OnClickListener {
                Timber.w("Terms of Use pressed")
            })
        )
    }

    private fun watchInputFieldsAndCheckBox() {
        tcCheckBox.setOnCheckedChangeListener { _, _ ->
            updateSignUpButtonState()
        }
        emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = emailInput.text.toString()
                if (text.isNotEmpty()) {
                    verifyEmail(text)
                }
            }
        }
        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val password = passwordInput.text.toString()
                val cnfPassword = cnfPasswordInput.text.toString()
                if (password.isNotEmpty() && cnfPassword.isNotEmpty()) {
                    verifyPassword(password,cnfPassword)
                }
            }
        }
    }

    private fun fireBaseRegisterUser(){


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                firebaseAuth.currentUser?.sendEmailVerification()
                val user = FirebaseAuth.getInstance().currentUser
                val emailVerified = user!!.isEmailVerified
                if(!emailVerified){
                    val intent = Intent(requireContext(), PendingVerification::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser =firebaseAuth.currentUser
        if (currentUser == null) {
            firebaseAuth.signOut()
        }
    }

    private fun verifyEmailPassword(email:String,password:String,cnfPassword:String):Boolean {
        val emailLayout = view?.findViewById<TextInputLayout>(R.id.email_input_text_layout)
        val passwordLayout = view?.findViewById<TextInputLayout>(R.id.password_input_text_layout)
        if(!Utility.isEmailValid(email)){
            emailLayout?.error = getString(R.string.invalid_email)
            return false
        }else {
            emailLayout?.isErrorEnabled = false
            this.email=emailInput.text.toString()
        }
        if(!Utility.isValidPassword(password)){
            passwordLayout?.error = getString(R.string.invalid_password_format)
            return false
        } else {
            passwordLayout?.isErrorEnabled = false
            this.password = passwordInput.text.toString()
        }
        if(password!=cnfPassword){
            passwordLayout?.error = getString(R.string.password_mismatch)
            return false
        } else passwordLayout?.isErrorEnabled = false
        return true
    }

    private fun updateSignUpButtonState() {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        val cnfPassword = cnfPasswordInput.text.toString()
        val isTcChecked = tcCheckBox.isChecked
        val isInputValid = verifyEmailPassword(email, password, cnfPassword)
        signUpBtn.isEnabled = isInputValid && isTcChecked
    }

    private fun verifyEmail(email:String):Boolean {
        val emailLayout = view?.findViewById<TextInputLayout>(R.id.email_input_text_layout)
        if(!Utility.isEmailValid(email)){
            emailLayout?.error = getString(R.string.invalid_email)
            return false
        }else {
            emailLayout?.isErrorEnabled = false
            this.email=emailInput.text.toString()
        }
        return true
    }

    private fun verifyPassword(password:String,cnfPassword:String):Boolean {
        val passwordLayout = view?.findViewById<TextInputLayout>(R.id.password_input_text_layout)
        if(!Utility.isValidPassword(password)){
            passwordLayout?.error = getString(R.string.invalid_password_format)
            return false
        } else {
            passwordLayout?.isErrorEnabled = false
            this.password = passwordInput.text.toString()
        }
        if(password!=cnfPassword){
            passwordLayout?.error = getString(R.string.password_mismatch)
            return false
        } else passwordLayout?.isErrorEnabled = false
        return true
    }
}