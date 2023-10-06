package com.uchi.resqsync.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE


object PrefConstant {
    const val onboardingPref = "PREFERENCES_FILE_KEY"
    const val firstTimeOpening = "FIRST_TIME_OPENING_KEY"
    const val detailsPref = "USER_DETAILS_FILLED"

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

    const val ERROR_DIALOG_REQUEST = 5001
    const val PERMISSIONS_REQUEST_ACCESS_LOCATION = 5002
    const val PERMISSIONS_REQUEST_ENABLE_GPS = 5003

}