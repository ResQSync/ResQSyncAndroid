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

package com.uchi.resqsync.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.uchi.resqsync.R
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.ui.dashboard.DashBoardActivity
import com.uchi.resqsync.ui.dialog.LoadingDialog
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import timber.log.Timber

//we are using activity here because in future we want to use a image cropper to allow the user to crop their images
class ProfileActivity : AppCompatActivity() {

    private lateinit var profilePictureImageView: ShapeableImageView
    private lateinit var updateButton: MaterialButton
    private lateinit var userName: TextInputEditText
    private lateinit var userEmail: TextInputEditText
    private lateinit var userNameLayout: TextInputLayout
    private lateinit var userEmailLayout: TextInputLayout
    private lateinit var loadingDialog: LoadingDialog

     private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Timber.d("PhotoPicker", "Selected URI: $uri")
                profilePictureImageView.apply {
                }
            } else {
                Timber.d("PhotoPicker", "No media selected")
            }
        }
        // Launch the photo picker and let the user choose only images.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        profilePictureImageView = findViewById(R.id.action_profile_picture)
        updateButton = findViewById(R.id.update_button)
        userName = findViewById(R.id.user_name_display)
        userEmail = findViewById(R.id.user_email)
        userEmailLayout = findViewById(R.id.email_input_text_layout)
        userNameLayout = findViewById(R.id.name_input_text_layout)
        editImage()
        setUpUserDetails()

    }



    private fun setUpUserDetails() {
        val user = PrefConstant.getUserDetails(this)
        userName.setText(user.name)
        userEmail.setText(user.email)
            userEmail.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        updateButton.isEnabled=true
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

        userName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateButton.isEnabled=true
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        updateButton.setOnClickListener {
            loadingDialog = LoadingDialog(this).apply {
                showLoadingDialog()
            }
            PrefConstant.updateUserDetails(this, UserModel(userName.text.toString(),userEmail.text.toString(),user.userId))
            FirebaseUtils().currentUserDetails().set(PrefConstant.getUserDetails(this)).addOnCompleteListener { task->
                if (task.isSuccessful){
                    loadingDialog.dismissDialog()
                }
            }
        }

    }

    private fun editImage() {
        profilePictureImageView.setOnClickListener {
            CroppingActivity.start(this)
        }
    }






}