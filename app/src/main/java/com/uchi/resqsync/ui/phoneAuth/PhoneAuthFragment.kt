package com.uchi.resqsync.ui.phoneAuth

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
            //TODO: use this to set the user name and email in settings
//            FirebaseUtils().currentUserDetails().get().addOnCompleteListener{ task ->
//            if(task.isSuccessful){
//                 val userModel=task.result.toObject(UserModel::class.java)
//                if(userModel!=null){
//                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
//                    startActivity(intent)
//                }else{
//                    navController.navigate(R.id.action_phoneAuthFragment_to_userDetailsFragment)
//                }
//             }
//          }
        }
        firebaseCallback()
        phoneNumber = view.findViewById(R.id.user_phone)
        ccp = view.findViewById(R.id.country_code_picker)
        proceedOTP = view.findViewById(R.id.send_otp_button)

        countryCode = ccp.selectedCountryCode

        proceedOTP.setOnClickListener {
            sendVerificationCode("+${countryCode}${phoneNumber.text.toString()}", firebaseAuth, false )
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

//    fun getUserName(){
//        FirebaseUtils().currentUserDetails().get().addOnCompleteListener{
//            task ->
//            if(task.isSuccessful){
//
//            }
//        }
//
//    }
}

//       val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                signInWithPhoneAuthCredential(credential)
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e)
//
//                if (e is FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                } else if (e is FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                    // reCAPTCHA verification attempted with null Activity
//                }
//
//                // Show a message and update the UI
//            }
//
//            override fun onCodeSent(
//                verificationId: String,
//                token: PhoneAuthProvider.ForceResendingToken,
//            ) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//
//
//                // Save verification ID and resending token so we can use them later
//                storedVerificationId = verificationId
//                resendToken = token
//            }
//        }
//
//        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//            .setPhoneNumber(phoneNumber.text.toString())
//            .setTimeout(3*60L, TimeUnit.SECONDS) // Timeout and unit
//            .setActivity(activity.) // Activity (for callback binding)
//            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//
//                    val user = task.result?.user
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                    // Update UI
//                }
//            }
//    }

