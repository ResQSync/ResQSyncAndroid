package com.uchi.resqsync.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent

class DoubleTapGestureListener(val context: Context, val onDoubleTap: () -> Unit) : GestureDetector.SimpleOnGestureListener() {
    override fun onDoubleTap(e: MotionEvent): Boolean {
        onDoubleTap.invoke()
        return true
    }
}
