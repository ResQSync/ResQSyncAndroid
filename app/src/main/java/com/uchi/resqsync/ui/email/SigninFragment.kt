package com.uchi.resqsync.ui.email

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.uchi.resqsync.R
import com.uchi.resqsync.utils.Utility.makeLinks
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.ui.phoneAuth.PhoneAuthFragment
import timber.log.Timber

class SigninFragment : Fragment() {
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signinBtn : MaterialButton
    private lateinit var newUserText: TextView
    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailInput = view.findViewById(R.id.user_email)
        passwordInput = view.findViewById(R.id.user_password)
        signinBtn = view.findViewById(R.id.signin_button)
        newUserText = view.findViewById(R.id.new_user_text)
        newUserText.highlightColor = Color.TRANSPARENT;
        newUser()

        signinBtn.setOnClickListener {
            initiateSignIn()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button press as needed
                // In this case, simply navigate back to the activity
                requireActivity().onBackPressed()
            }
        })

    }

    private fun initiateSignIn() {
        val intent = Intent(requireContext(), DashBoardActivity::class.java)
        startActivity(intent)
        Timber.d("Signin button pressed")
//        val signin = BackendService.backendInstance.singInUser(LoginRequest(email,password))
//        signin.enqueue(object : Callback<LoginResponse>{
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                Timber.d("Signin API successfully called")
//
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Timber.d("Error calling Signin API")
//            }
//        })
    }

    private fun newUser() {
        newUserText.makeLinks(
            Pair("Register", View.OnClickListener {
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, PhoneAuthFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            })
        )
    }
}