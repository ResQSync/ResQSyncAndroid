package com.uchi.resqsync.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

import com.uchi.resqsync.R

import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.utils.FirebaseUtils

class CreateCircleDialog():DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var circleName: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        dialogView = layoutInflater.inflate(R.layout.create_circle_dialog_layout, null)
        circleName = dialogView.findViewById(R.id.circle_name_input_text)
        return  MaterialAlertDialogBuilder(requireContext()).run {
            this.setView(dialogView)
            this.setTitle(resources.getString(R.string.create_circle_title))
            this.setMessage(resources.getString(R.string.create_circle_message))
            this.setPositiveButton(resources.getString(R.string.add)) { _, _ ->
                if(circleName.text.toString().isNotEmpty()){
                    val cName =circleName.text.toString()
                    FirebaseUtils().createCollection(cName)
                    FirebaseUtils().userCircleDetails(cName)
                        .set(UserCircleModel(listOf(UserModel("","","")))).addOnCompleteListener {
                            dismiss()
                        }
                }
            }
            this.setNegativeButton(resources.getString(R.string.add)){_,_ ->
                dismiss()
            }

            this.create()
        }
    }

}
