package edu.cit.poliquit.aquahaven.model

import java.io.Serializable

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(
    val firstname: String, val lastname: String,
    val email: String, val password: String, val phone: String
)

data class AuthResponse(
    val success: Boolean, val user: UserInfo?,
    val accessToken: String?, val refreshToken: String?,
    val message: String?, val errorCode: String?
)

data class UserInfo(
    val email: String?, val firstname: String?,
    val lastname: String?, val role: String?
) : Serializable

data class UserProfile(
    val bio: String = "",
    val photoUri: String = "",
    val phone: String = "",
    val location: String = ""
) : Serializable

data class Category(val id: Long, val name: String, val slug: String, val icon: String, val sortOrder: Int) : Serializable

data class CategoryListResponse(val success: Boolean, val data: List<Category>?)

data class Product(
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String?,
    val stock: Int,
    val waterType: String?,
    val categoryId: Long = 0,
    val categoryName: String?,
    val categorySlug: String?,
    val categoryIcon: String? = null,
    val active: Boolean = true,
    val createdAt: String? = null
) : Serializable {
    fun categoryIcon(): String = categoryIcon ?: when (categorySlug) {
        "freshwater-fish" -> "🐠"; "saltwater-fish" -> "🐡"
        "aquatic-plants"  -> "🌿"; "fish-food"      -> "🫙"
        "equipment"       -> "⚙️"; "coral-marine"   -> "🪸"
        "decorations"     -> "🪨"; else             -> "📦"
    }
}

data class ProductPage(val content: List<Product>, val totalElements: Int, val totalPages: Int, val number: Int)
data class ProductPageResponse(val success: Boolean, val data: ProductPage?)
data class ProductResponse(val success: Boolean, val data: Product?)

data class CartItem(val product: Product, var quantity: Int) : Serializable

data class PlaceOrderRequest(
    val items: List<OrderItemRequest>,
    val shippingAddress: String,
    val paymentMethod: String,
    val notes: String?
)
data class OrderItemRequest(val productId: Long, val quantity: Int)

data class Order(
    val id: Long,
    val orderRef: String,
    val status: String,
    val paymentMethod: String,
    val totalAmount: Double,
    val shippingAddress: String,
    val notes: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val items: List<OrderItem>
) : Serializable

data class OrderItem(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
) : Serializable

data class OrderResponse(val success: Boolean, val data: Order?, val message: String?)
data class OrderListResponse(val success: Boolean, val data: List<Order>?, val message: String?)
data class ApiResponse<T>(val success: Boolean, val data: T?, val message: String?, val errorCode: String?)