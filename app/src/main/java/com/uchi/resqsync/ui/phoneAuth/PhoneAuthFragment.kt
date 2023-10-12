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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.ui.dialog.LoadingDialog
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import timber.log.Timber
import java.util.concurrent.TimeUnit

class PhoneAuthFragment : Fragment() {
    lateinit var phoneNumber: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ccp: CountryCodePicker
    private lateinit var otp : String
    private lateinit var proceedOTP : MaterialButton
    private lateinit var countryCode: String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken

    private lateinit var navController: NavController
    private lateinit var loadingDialog: LoadingDialog

    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_phone_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        if(FirebaseUtils().isLoggedIn()){
            if(PrefConstant.getDetailsProvided(requireContext())){
                val intent = Intent(requireContext(), DashBoardActivity::class.java)
                startActivity(intent)
            }else{
                navController.navigate(R.id.action_phoneAuthFragment_to_userDetailsFragment)
            }
        }
        firebaseCallback()
        phoneNumber = view.findViewById(R.id.user_phone)
        ccp = view.findViewById(R.id.country_code_picker)
        proceedOTP = view.findViewById(R.id.send_otp_button)
        proceedOTP.isEnabled=false
        countryCode = ccp.selectedCountryCode
        handlePhoneSubmit()

    }

    private fun handlePhoneSubmit() {
        phoneNumber.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun onTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    proceedOTP.isEnabled = string.toString().length==10
            }

            override fun afterTextChanged(p0: Editable?) {
              // Do nothing
            }

        })
        proceedOTP.setOnClickListener {
            sendVerificationCode("+${countryCode}${phoneNumber.text.toString()}", firebaseAuth, false )
            loadingDialog= LoadingDialog(requireActivity())
            loadingDialog.showLoadingDialog()
        }
    }

    fun firebaseCallback() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                // on below line updating message
                // and displaying toast message
                Timber.d("Verification successful")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Timber.d("Verification Failed Retry!!", p0)
            }

            override fun onCodeSent(
                verificationId: String,
                resendVerification: PhoneAuthProvider.ForceResendingToken,
            ) {
                super.onCodeSent(verificationId, resendVerification)
                resendToken = resendVerification
                otp = verificationId
                val bundle = Bundle()
                bundle.putString("verificationCode", otp)
                loadingDialog.dismissDialog()
                navController.navigate(R.id.action_phoneAuthFragment_to_otpFragment, bundle)
            }
        }
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    }

    fun sendVerificationCode(
        number: String,
        auth: FirebaseAuth,
        isResend : Boolean,
    ) {
        Timber.d("sendVerificationCode: $number")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)

        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendToken).build())
        }else{
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        }
    }
}
