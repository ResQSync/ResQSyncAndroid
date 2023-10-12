package com.uchi.resqsync.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.uchi.resqsync.ui.onboarding.OnboardingActivity

//TODO: delete this activity
class ResQSync : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.uchi.resqsync.R.layout.activity_res_qsync)
        val btn = findViewById<Button>(com.uchi.resqsync.R.id.test)
        btn.setOnClickListener {
            // Create an Intent to launch the SecondActivity
            val intent = Intent(this@ResQSync, OnboardingActivity::class.java)
            startActivity(intent)
        }
    }


}
