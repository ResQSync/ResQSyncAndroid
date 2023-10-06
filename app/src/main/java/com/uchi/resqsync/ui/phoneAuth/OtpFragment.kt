package com.uchi.resqsync.ui.phoneAuth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.uchi.resqsync.R
import com.uchi.resqsync.snackbar.BaseSnackbarBuilderProvider
import com.uchi.resqsync.snackbar.SnackbarBuilder
import com.uchi.resqsync.snackbar.showSnackbar
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.ui.onboarding.OnboardingActivity
import com.uchi.resqsync.utils.UIUtils
import timber.log.Timber


class OtpFragment : Fragment() {
    private lateinit var otpVerifyButton: MaterialButton
    private lateinit var userOTP: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        otpVerifyButton = view.findViewById(R.id.otp_verification_button)
        userOTP = view.findViewById(R.id.user_otp)
        navController = Navigation.findNavController(view)

        otpVerifyButton.setOnClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val enteredOTP = userOTP.text.toString()
            val verificationCode = arguments?.getString("verificationCode")?: "null"
            val credentail = PhoneAuthProvider.getCredential(verificationCode,enteredOTP)
         signInWithPhoneAuthCredential(credentail,firebaseAuth)
        }

    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        auth: FirebaseAuth
    ) {
        // on below line signing with credentials.
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                // displaying toast message when
                // verification is successful
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_otpFragment_to_userDetailsFragment)
                    Timber.d("signInWithPhoneAuthCredential: /..../...changed")
                } else {
                    // Sign in failed, display a message
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code
                        // entered was invalid
                        UIUtils.showThemedToast(requireContext(),"invalid otp",false)
                    }
                }
            }
    }

}