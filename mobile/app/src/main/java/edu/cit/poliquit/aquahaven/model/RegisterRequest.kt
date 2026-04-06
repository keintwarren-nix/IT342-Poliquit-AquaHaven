package edu.cit.poliquit.aquahaven.model

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val phone: String
)