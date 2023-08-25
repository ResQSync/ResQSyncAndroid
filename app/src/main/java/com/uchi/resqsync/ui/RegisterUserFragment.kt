package com.uchi.resqsync.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.uchi.resqsync.R
import com.uchi.resqsync.utils.Utility

class RegisterUserFragment : Fragment() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var cnfPasswordInput: TextInputEditText
    private lateinit var signUpBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailInput = view.findViewById(R.id.user_email)
        passwordInput = view.findViewById(R.id.user_password)
        cnfPasswordInput = view.findViewById(R.id.cnf_user_password)
        signUpBtn = view.findViewById(R.id.signup_button)
        signUpBtn.setOnClickListener {
            if(!verifyEmailPassword(emailInput.text.toString(),passwordInput.text.toString(),cnfPasswordInput.text.toString())){
                return@setOnClickListener
            }
        }

    }

    private fun verifyEmailPassword(email:String,password:String,cnfPassword:String):Boolean {
        val emailLayout = view?.findViewById<TextInputLayout>(R.id.email_input_text_layout)
        val passwordLayout = view?.findViewById<TextInputLayout>(R.id.password_input_text_layout)
        if(!Utility.isEmailValid(email)){
            emailLayout?.error = getString(R.string.invalid_email)
            return false
        }else emailLayout?.isErrorEnabled = false
        if(!Utility.isValidPassword(password)){
            passwordLayout?.error = getString(R.string.invalid_password_format)
            return false
        } else passwordLayout?.isErrorEnabled = false
        if(password!=cnfPassword){
            passwordLayout?.error = getString(R.string.password_mismatch)
            return false
        } else passwordLayout?.isErrorEnabled = false
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_user, container, false)
    }
}