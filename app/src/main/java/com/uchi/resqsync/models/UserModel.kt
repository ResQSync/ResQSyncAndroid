package com.uchi.resqsync.models

data class UserModel(
    val name: String,
    val email: String
) {
    constructor() : this("", "")
}