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
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

import com.uchi.resqsync.R

import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.utils.FirebaseUtils


//TODO: not active right now
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
                }
            }
            this.setNegativeButton(resources.getString(R.string.add)){_,_ ->
                dismiss()
            }

            this.create()
        }
    }

}
