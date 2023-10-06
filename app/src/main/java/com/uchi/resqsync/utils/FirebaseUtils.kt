package com.uchi.resqsync.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

public class FirebaseUtils {

    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    fun isLoggedIn(): Boolean {
        return currentUserId() != null
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun currentUserDetails(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId()?:"")
    }

    fun currentUserLocationDetails() : DocumentReference{
        return FirebaseFirestore.getInstance().collection("userLocation").document(currentUserId()?:"")
    }
}