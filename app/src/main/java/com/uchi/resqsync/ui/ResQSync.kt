package com.uchi.resqsync.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.uchi.resqsync.R

class ResQSync : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_qsync)
        val btn = findViewById<Button>(R.id.test)
        btn.setOnClickListener {
            val fragment = RegisterUserFragment() // Instantiate your Fragment
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
            fragmentTransaction.commit()
        }


    }
}