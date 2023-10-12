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

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uchi.resqsync.R
import com.uchi.resqsync.models.JoiningCode
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.Utility
import timber.log.Timber

class UniqueCodeDialog: DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var codeView:TextView
    private var positiveButtonAction: (() -> Unit)? = null
    private var showResetButton: Boolean = false
    private var cancellable:Boolean=false
    private lateinit var copyCodeButton:ImageView
    private lateinit var shareCodeButton:ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        dialogView = layoutInflater.inflate(R.layout.unique_code_dialog, null)
        codeView = dialogView.findViewById(R.id.unique_code_text)
        getJoiningCode()
        copyCodeButton = dialogView.findViewById(R.id.action_copy_code)
        shareCodeButton = dialogView.findViewById(R.id.action_share_code)

        copyCodeButton.setOnClickListener {
            Utility.copyTextToClipboard(codeView.text.toString(),"Unique Code", requireActivity())
        }
        shareCodeButton.setOnClickListener {
                Utility.shareAction(resources.getString(R.string.introducing_your_unique_code,codeView.text.toString()),"Unique Joining Code",requireContext())
        }
        return  MaterialAlertDialogBuilder(requireContext()).run {
            this.setView(dialogView)
            this.setTitle(resources.getString(R.string.joining_code_title))
            this.setMessage(resources.getString(R.string.joining_code_message))
            this.setPositiveButton(resources.getString(R.string.done)) { _, _ ->
                if(positiveButtonAction==null){
                    dismiss()
                }
                positiveButtonAction?.invoke()

            }
            if(showResetButton){
                this.setNegativeButton(resources.getString(R.string.reset)){ _,_->
                    FirebaseUtils().generateAndAddUniqueCode()
                }
            }
            this.setCancelable(cancellable)
            this.create()
        }
    }

    private fun getJoiningCode() {
        Timber.d("Get joining code")
        FirebaseUtils().uniqueCodeDetails().get().addOnCompleteListener {task->
            if(task.isSuccessful){
                val code = task.result.toObject(JoiningCode::class.java)
                codeView.text = code?.uniqueCode
            }
        }
    }

    companion object{
        fun newInstance(
            positiveButtonAction: (() -> Unit)?,
            showResetButton: Boolean = false,
            cancellable:Boolean=false
        ): UniqueCodeDialog {
            val dialog = UniqueCodeDialog()
            dialog.positiveButtonAction = positiveButtonAction
            dialog.showResetButton = showResetButton
            dialog.cancellable=cancellable
            return dialog
        }
    }

}