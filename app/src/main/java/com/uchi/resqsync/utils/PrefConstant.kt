/*
 *  Copyright (c) 2023 Sumit Singh <resqsync@gmail.com>
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

package com.uchi.resqsync.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserModel
import timber.log.Timber


object PrefConstant {
    const val onboardingPref = "PREFERENCES_FILE_KEY"
    const val firstTimeOpening = "FIRST_TIME_OPENING_KEY"
    const val detailsPref = "USER_DETAILS_FILLED"
    const val SHARED_PREF="SHARED_PREFERENCE"
    const val userCircle="USER_CIRCLE"
    const val circleName="CIRCLE_NAME"
    const val cName="currentCircle"

    fun getOnboardingSharedPref(context: Context): Boolean {
        return context.getSharedPreferences(onboardingPref, MODE_PRIVATE)
            .getBoolean(firstTimeOpening, false);
    }

    fun setOnboardingPref(key: String, context: Context?) {
        return context?.getSharedPreferences(onboardingPref, MODE_PRIVATE)?.edit()?.putBoolean(
            firstTimeOpening,
            true
        )!!.apply();
    }

    fun getUserDetails(context: Context):UserModel{
        val details= context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE).getString("user_details","")
        val gson=Gson()
        return gson.fromJson(details,UserModel::class.java)
    }

    fun updateUserDetails(context: Context,data:UserModel){
        val gson=Gson()
        val details =gson.toJson(data)
        context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
            .edit().putString("user_details",details).apply()
    }

    fun getDetailsProvided(context: Context):Boolean{
        return context.getSharedPreferences(detailsPref, MODE_PRIVATE)
            .getBoolean("FILLED", false);
    }

    fun setDetailsProvided(context: Context?){
        return context?.getSharedPreferences(detailsPref, MODE_PRIVATE)?.edit()?.putBoolean(
            "FILLED",
            true
        )!!.apply();

    }

    fun saveMembersDetails(context: Context,preferenceStringName:String, data:UserCircleModel){
        val gson = Gson()
        val userCircleModelJson = gson.toJson(data)
        val sharedPreferences = context.getSharedPreferences(userCircle, MODE_PRIVATE)
        sharedPreferences.edit().putString(preferenceStringName, userCircleModelJson).apply()

    }

    fun getMembersDetails(context: Context,preferenceStringName: String):UserCircleModel?{
        val sharedPreferences = context.getSharedPreferences(userCircle, MODE_PRIVATE)
        val userCircleModelJson = sharedPreferences.getString(preferenceStringName,"")
        val gson = Gson()
        if(userCircleModelJson.isNullOrEmpty()){
            return null
        }
        return try {
            gson.fromJson(userCircleModelJson, UserCircleModel::class.java)
        } catch (e: JsonSyntaxException) {
            Timber.e(e.message)
            null
        }

    }

    fun updateCurrentCircle(context: Context,name: String){
        val sharedPreferences = context.getSharedPreferences(circleName, MODE_PRIVATE)
        sharedPreferences.edit().putString(cName,name).apply()
    }

    fun getCurrentCircle(context: Context):String?{
        return context.getSharedPreferences(circleName, MODE_PRIVATE).getString(cName,"")
    }

    const val ERROR_DIALOG_REQUEST = 5001
    const val PERMISSIONS_REQUEST_ACCESS_LOCATION = 5002
    const val PERMISSIONS_REQUEST_ENABLE_GPS = 5003

}