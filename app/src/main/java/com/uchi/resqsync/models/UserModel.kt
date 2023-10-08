package com.uchi.resqsync.models

data class UserModel(
    val name: String,
    val email: String,
    val userId:String
) {
    constructor() : this("", "","")
}