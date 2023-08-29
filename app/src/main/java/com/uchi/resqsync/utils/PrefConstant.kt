package com.uchi.resqsync.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE


object PrefConstant {
    const val onboardingPref = "PREFERENCES_FILE_KEY"
    const val firstTimeOpening = "FIRST_TIME_OPENING_KEY"

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

}