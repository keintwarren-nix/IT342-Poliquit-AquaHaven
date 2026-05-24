package edu.cit.poliquit.aquahaven.network

import edu.cit.poliquit.aquahaven.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // AUTH
    @POST("api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    // CATEGORIES
    @GET("api/v1/categories")
    suspend fun getCategories(): Response<CategoryListResponse>

    // PRODUCTS
    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("keyword") keyword: String? = null,
        @Query("categorySlug") categorySlug: String? = null,
        @Query("waterType") waterType: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<ProductPageResponse>

    @GET("api/v1/products/{id}")
    suspend fun getProduct(
        @Path("id") id: Long
    ): Response<ProductResponse>

    // ORDERS (AUTH REQUIRED)
    @POST("api/v1/orders")
    suspend fun placeOrder(
        @Header("Authorization") auth: String,
        @Body request: PlaceOrderRequest
    ): Response<OrderResponse>

    @GET("api/v1/orders")
    suspend fun myOrders(
        @Header("Authorization") auth: String
    ): Response<OrderListResponse>

    @GET("api/v1/orders/{ref}")
    suspend fun getOrder(
        @Header("Authorization") auth: String,
        @Path("ref") ref: String
    ): Response<OrderResponse>
}