package com.uchi.resqsync.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import com.uchi.resqsync.R

class LoadingDialog(private val activity: Activity) {
    private lateinit var dialog: AlertDialog

    fun showLoadingDialog(){
        val builder=AlertDialog.Builder(activity,R.style.TransparentAlertDialog)
        val inflater =activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog,null))
        builder.setCancelable(false)
        dialog=builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}