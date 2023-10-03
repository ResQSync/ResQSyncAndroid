package com.uchi.resqsync.ui.email

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.uchi.resqsync.R
import com.uchi.resqsync.service.EmailStatusViewModel
import com.uchi.resqsync.service.EmailVerificationService
import com.uchi.resqsync.ui.dashboard.DashBoardActivity

import kotlinx.coroutines.launch
import timber.log.Timber

class PendingVerification : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_verification)
        val serviceIntent = Intent(this, EmailVerificationService::class.java)
        startService(serviceIntent)
        getStatus()



    }

    fun getStatus(){
        val viewModel: EmailStatusViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(EmailStatusViewModel::class.java)
        lifecycleScope.launch {
            viewModel.getData().collect { status ->
                if (status) {
                    Timber.d("Activity Code5")
                    val loadingLottie = findViewById<LottieAnimationView>(R.id.loading)
                    loadingLottie.visibility = View.GONE
                    val done = findViewById<LottieAnimationView>(R.id.success)
                    done.visibility = View.VISIBLE
                    val continueBtn = findViewById<MaterialButton>(R.id.continue_button)
                    continueBtn.visibility = View.VISIBLE

                    continueBtn.setOnClickListener {
                        val intent = Intent(this@PendingVerification, DashBoardActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@PendingVerification, "not", Toast.LENGTH_SHORT).show()
                    Timber.w("Not verfied")
                }

            }
        }


    }

}
//
//viewModel.currentStatus.observe(this@PendingVerification, Observer<Boolean> { status ->
//
//    if (status) {
//        Timber.d("Activity Code5")
//        val loadingLottie = findViewById<LottieAnimationView>(R.id.loading)
//        loadingLottie.visibility = View.GONE
//        val done = findViewById<LottieAnimationView>(R.id.success)
//        done.visibility = View.VISIBLE
//        val continueBtn = findViewById<MaterialButton>(R.id.continue_button)
//        continueBtn.visibility = View.VISIBLE
//
//        continueBtn.setOnClickListener {
//            val intent = Intent(this@PendingVerification, DashBoardActivity::class.java)
//            startActivity(intent)
//        }
//    } else {
//        Toast.makeText(this, "not", Toast.LENGTH_SHORT).show()
//        Timber.w("Not verfied")
//    }
//})