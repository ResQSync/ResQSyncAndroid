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

package com.uchi.resqsync.ui.phoneAuth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.snackbar.BaseSnackbarBuilderProvider
import com.uchi.resqsync.snackbar.SnackbarBuilder
import com.uchi.resqsync.snackbar.showSnackbar
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.ui.dialog.LoadingDialog
import com.uchi.resqsync.ui.onboarding.OnboardingActivity
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.UIUtils
import timber.log.Timber


class OtpFragment : Fragment() {
    private lateinit var otpVerifyButton: MaterialButton
    private lateinit var userOTP: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var otpInputLayout:TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        otpVerifyButton = view.findViewById(R.id.otp_verification_button)
        otpVerifyButton.isEnabled=false
        userOTP = view.findViewById(R.id.user_otp)
        navController = Navigation.findNavController(view)
        otpInputLayout= view.findViewById(R.id.otp_input_text_layout)
        handleOTPVerification()
    }

    private fun handleOTPVerification() {
        userOTP.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {
                otpVerifyButton.isEnabled = s.toString().length==6
                otpInputLayout.isErrorEnabled=false
            }
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int,
                                           after:Int) {
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        otpVerifyButton.setOnClickListener {
            if(userOTP.text.toString().isNotEmpty()){
                loadingDialog = LoadingDialog(requireActivity()).apply {
                    showLoadingDialog()
                }
                firebaseAuth = FirebaseAuth.getInstance()
                val enteredOTP = userOTP.text.toString()
                val verificationCode = arguments?.getString("verificationCode") ?: "null"
                val credentail = PhoneAuthProvider.getCredential(verificationCode, enteredOTP)
                signInWithPhoneAuthCredential(credentail, firebaseAuth)
            }else{
                otpInputLayout.isErrorEnabled=true
                otpInputLayout.error= getString(R.string.enter_otp_error)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential, auth: FirebaseAuth
    ) {
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Timber.d("OTP verification successful")
                    isExistingUser { exists->
                        if (exists){
                            loadingDialog.dismissDialog()
                            val intent = Intent(requireContext(), DashBoardActivity::class.java)
                            startActivity(intent)
                        }else{
                            loadingDialog.dismissDialog()
                            navController.navigate(R.id.action_otpFragment_to_userDetailsFragment)
                        }
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        loadingDialog.dismissDialog()
                        otpInputLayout.isErrorEnabled=true
                        otpInputLayout.error=getString(R.string.enter_otp_error)
                    }
                }
            }
    }

    private fun isExistingUser(callback: (Boolean) -> Unit) {
        Timber.i("Checking if user exist")
        FirebaseUtils().getUserDetails().document(FirebaseUtils().currentUserId() ?: "").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userDetails = task.result.toObject(UserModel::class.java)
                    if ((userDetails != null)) {
                        PrefConstant.updateUserDetails(
                            requireContext(), UserModel(
                                userDetails.name, userDetails.email, userDetails.userId
                            )
                        )
                        callback(true)
                    } else callback(false)
                }else callback (false)
            }
    }

}