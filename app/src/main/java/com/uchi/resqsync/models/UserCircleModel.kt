package com.uchi.resqsync.models

import com.google.firebase.firestore.auth.User

data class UserCircleModel(
    val familyMembers: List<UserModel>,
) {
}