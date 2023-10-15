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
package com.uchi.resqsync.ui.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.uchi.resqsync.R
import com.uchi.resqsync.ui.dashboard.LocationMapFragment
import com.uchi.resqsync.ui.phoneAuth.PhoneAuthFragment
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.UIUtils

class OnboardingFragment2 : Fragment() {
    private lateinit var navController: NavController
    private lateinit var getStartedButton: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.onboarding_about2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStartedButton = view.findViewById(R.id.get_started_btn)
        navController = Navigation.findNavController(view)

        getStartedButton.setOnClickListener {
            checkRuntimePermissions()
        }
    }

    fun checkRuntimePermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED
            ) {
                UIUtils.showThemedToast(requireContext(), "Permission granted", false)
                PrefConstant.setOnboardingPref(PrefConstant.firstTimeOpening, activity)
                navController.navigate(R.id.action_onboardingFragment2_to_phoneAuthFragment)

            } else {
                UIUtils.showThemedToast(requireContext(), "Please give required permissions", false)
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        COARSE_LOCATION, FINE_LOCATION,
                        CALL_PHONE, BACKGROUND_LOCATION
                    ), APP_PERMISSION_REQUEST_CODE
                )
            }
        }else{
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED) {
                UIUtils.showThemedToast(requireContext(), "Permission granted", false)
                PrefConstant.setOnboardingPref(PrefConstant.firstTimeOpening, activity)
                navController.navigate(R.id.action_onboardingFragment2_to_phoneAuthFragment)

            } else {
                UIUtils.showThemedToast(requireContext(), "Please give required permissions", false)
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        COARSE_LOCATION, FINE_LOCATION,
                        CALL_PHONE), APP_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== APP_PERMISSION_REQUEST_CODE){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if (grantResults.isNotEmpty()){
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED
                        && grantResults[2]== PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED){
                        PrefConstant.setOnboardingPref(PrefConstant.firstTimeOpening, activity)
                        navController.navigate(R.id.action_onboardingFragment2_to_phoneAuthFragment)
                    }else{
                        showPermissionDialog()
                        UIUtils.showThemedToast(requireContext(),"Required permission denied",false)
                    }
                }
            }else{
                if (grantResults.isNotEmpty()){
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]== PackageManager.PERMISSION_GRANTED
                        && grantResults[2]== PackageManager.PERMISSION_GRANTED){
                        PrefConstant.setOnboardingPref(PrefConstant.firstTimeOpening, activity)
                        navController.navigate(R.id.action_onboardingFragment2_to_phoneAuthFragment)
                    }else{
                        showPermissionDialog()
                        UIUtils.showThemedToast(requireContext(),"Required permission denied",false)
                    }
                }
            }
        }else{
            showPermissionDialog()
            checkRuntimePermissions()
        }
    }

    fun showPermissionDialog(){
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .create()
        builder.apply {
            setTitle(resources.getString(R.string.permissions_title))
            setMessage(resources.getString(R.string.permission_message))
            setCanceledOnTouchOutside(false)
            setButton(AlertDialog.BUTTON_POSITIVE,"Allow") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                startActivity(intent)
            }
        }
        builder.show()

    }






    companion object{
        @RequiresApi(Build.VERSION_CODES.Q)
        const val BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION
        const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        const val CALL_PHONE = Manifest.permission.CALL_PHONE
        const val APP_PERMISSION_REQUEST_CODE=2023
    }

}