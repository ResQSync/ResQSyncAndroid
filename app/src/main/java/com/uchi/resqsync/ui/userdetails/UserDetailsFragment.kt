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

package com.uchi.resqsync.ui.userdetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.ui.dialog.LoadingDialog
import com.uchi.resqsync.ui.dialog.UniqueCodeDialog
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.Utility

class UserDetailsFragment : Fragment() {
    private lateinit var submitDetail : MaterialButton
    private lateinit var userFullName: TextInputEditText
    private lateinit var userEmail : TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitDetail = view.findViewById(R.id.submit_details_button)
        userFullName= view.findViewById(R.id.user_full_name)
        userEmail = view.findViewById(R.id.user_email)
        nameInputLayout = view.findViewById(R.id.fullname_input_text_layout)
        emailInputLayout = view.findViewById(R.id.email_input_text_layout)

        submitDetail.setOnClickListener {
            loadingDialog = LoadingDialog(requireActivity()).apply {
                showLoadingDialog()
            }
            if(!Utility.isEmailValid(userEmail.text.toString())){
                emailInputLayout.error = getString(R.string.invalid_email)
                return@setOnClickListener
            }
            if(userFullName.text.toString().isEmpty()){
                nameInputLayout.error= getString(R.string.name_required)
                return@setOnClickListener
            }
            updateDetails()
        }

    }

    private fun updateDetails() {
        FirebaseUtils().currentUserDetails().set(
            UserModel(
                userFullName.text.toString(),
                userEmail.text.toString(),
                FirebaseUtils().currentUserId().toString()
            )
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                PrefConstant.setDetailsProvided(requireContext())

                PrefConstant.updateUserDetails(
                    requireContext(), UserModel(
                        userFullName.text.toString(),
                        userEmail.text.toString(),
                        FirebaseUtils().currentUserId().toString()
                    )
                )
                FirebaseUtils().generateAndAddUniqueCode()
                loadingDialog.dismissDialog()
                val dialog = UniqueCodeDialog.newInstance(positiveButtonAction = {
                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
                    startActivity(intent)
                }, false)
                dialog.show(parentFragmentManager, "UniqueCodeDialog")
            } else {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

}