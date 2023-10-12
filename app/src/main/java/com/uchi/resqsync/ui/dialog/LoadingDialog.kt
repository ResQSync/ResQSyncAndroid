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