package com.uchi.resqsync.utils.api

data class RegistrationRequest(
    val email: String,
    val password: String
)

data class RegistrationResponse(
    val status: Boolean,
    val token: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: Boolean,
    val token: String
)