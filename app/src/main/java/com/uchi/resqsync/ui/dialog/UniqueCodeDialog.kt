package com.uchi.resqsync.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uchi.resqsync.R
import com.uchi.resqsync.models.JoiningCode
import com.uchi.resqsync.utils.FirebaseUtils
import timber.log.Timber

class UniqueCodeDialog: DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var codeView:TextView
    private var positiveButtonAction: (() -> Unit)? = null
    private var showResetButton: Boolean = false
    private var cancellable:Boolean=false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        dialogView = layoutInflater.inflate(R.layout.unique_code_dialog, null)
        codeView = dialogView.findViewById(R.id.unique_code_text)
        getJoiningCode()
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