package edu.cit.poliquit.aquahaven.model

data class AuthResponse(
    val success: Boolean,
    val data: AuthData?,
    val error: ErrorInfo?,
    val timestamp: String?
)

data class AuthData(
    val user: UserInfo?,
    val accessToken: String?,
    val refreshToken: String?
)

data class UserInfo(
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val role: String?
)

data class ErrorInfo(
    val code: String?,
    val message: String?
)