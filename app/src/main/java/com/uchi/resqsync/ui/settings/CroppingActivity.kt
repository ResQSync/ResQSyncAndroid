package com.uchi.resqsync.ui.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.uchi.resqsync.R
import timber.log.Timber

class CroppingActivity : CropImageActivity() {

    private lateinit var topAppBarLayout: MaterialToolbar
    private lateinit var cropImageView: CropImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cropping)
        setCropImageView(findViewById(R.id.cropImageView))
        cropImageView= findViewById(R.id.cropImageView)
        topAppBarLayout=findViewById(R.id.topAppBar)
        handleMenu()
    }

    private fun handleMenu() {
        topAppBarLayout.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        topAppBarLayout.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_save -> {
                    cropImage()
                    true
                }
                R.id.action_rotate ->{
                    onRotateClick()
                    true
                }
                else -> false
            }
        }
    }

    override fun setContentView(view: View) {
        super.setContentView(R.layout.activity_cropping)
    }

    override fun onPickImageResult(resultUri: Uri?) {
        super.onPickImageResult(resultUri)
        if (resultUri != null) {

            cropImageView.setImageUriAsync(resultUri)
        }
    }

    override fun getResultIntent(uri: Uri?, error: java.lang.Exception?, sampleSize: Int): Intent {
        val result = super.getResultIntent(uri, error, sampleSize)
        // Adding some more information.
        return result.putExtra("EXTRA_KEY", "Extra data")
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val result = CropImage.ActivityResult(
            originalUri = cropImageView.imageUri,
            uriContent = uri,
            error = error,
            cropPoints = cropImageView.cropPoints,
            cropRect = cropImageView.cropRect,
            rotation = cropImageView.rotatedDegrees,
            wholeImageRect =cropImageView.wholeImageRect,
            sampleSize = sampleSize,
        )
        Timber.tag("AIC-Sample").i("Original bitmap: ${result.originalBitmap}")
        Timber.tag("AIC-Sample").i("Original uri: ${result.originalUri}")
        Timber.tag("AIC-Sample").i("Output bitmap: ${result.bitmap}")
        Timber.tag("AIC-Sample").i("Output uri: ${result.getUriFilePath(this)}")
        cropImageView.setImageUriAsync(result.uriContent)
    }

    override fun setResultCancel() {
        Timber.tag("AIC-Sample").i("User this override to change behaviour when cancel")
        super.setResultCancel()
    }

    override fun updateMenuItemIconColor(menu: Menu, itemId: Int, color: Int) {
        Timber.tag("AIC-Sample").i("If not using your layout, this can be one option to change colours")
        super.updateMenuItemIconColor(menu, itemId, color)
    }


    private fun onRotateClick() {
        cropImageView.rotateImage(90)
    }

    companion object {
        fun start(activity: Activity) {
            ActivityCompat.startActivity(
                activity,
                Intent(activity, CroppingActivity::class.java),
                null,
            )
        }
    }
}