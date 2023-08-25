package com.uchi.resqsync.utils

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utility {
    /** Checks if the input email is valid or not **/
    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!).matches()
    }
    /** Checks if the input password is valid or not **/
    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}