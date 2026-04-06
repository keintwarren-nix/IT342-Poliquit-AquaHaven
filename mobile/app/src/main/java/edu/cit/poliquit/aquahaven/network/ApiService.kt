package edu.cit.poliquit.aquahaven.network

import edu.cit.poliquit.aquahaven.model.AuthResponse
import edu.cit.poliquit.aquahaven.model.LoginRequest
import edu.cit.poliquit.aquahaven.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}