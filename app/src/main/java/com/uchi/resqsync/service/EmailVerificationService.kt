package com.uchi.resqsync.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import timber.log.Timber

class EmailVerificationService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val checkIntervalMillis = 1000L
    private var isEmailVerified = false
    private lateinit var viewModel: EmailStatusViewModel


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkEmailVerificationStatus()
        return START_STICKY
    }

    private fun checkEmailVerificationStatus() {
        coroutineScope.launch {
            val firebaseAuth = FirebaseAuth.getInstance()
            val user = firebaseAuth.currentUser
            var elapsedTime = 0L

            while (!isEmailVerified && elapsedTime < 5 * 60 * 1000) {
                user?.reload()
                val emailVerified = user?.isEmailVerified ?: false

                if (emailVerified) {
                    isEmailVerified = true
                    Timber.w("email is verfied $emailVerified")
                    launch(Dispatchers.Main) {
                        viewModel.updateData(true)
                    }
                }

                delay(checkIntervalMillis)
                elapsedTime += checkIntervalMillis
            }

            if (!isEmailVerified) {
                firebaseAuth.currentUser?.delete()
            }

            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}

