package edu.cit.poliquit.aquahaven.utils

import edu.cit.poliquit.aquahaven.model.CartItem
import edu.cit.poliquit.aquahaven.model.Product

object CartManager {
    private val items = mutableListOf<CartItem>()

    fun getItems(): List<CartItem> = items.toList()

    fun add(product: Product, qty: Int = 1) {
        val existing = items.find { it.product.id == product.id }
        if (existing != null) existing.quantity = minOf(existing.quantity + qty, product.stock)
        else items.add(CartItem(product, qty))
    }

    fun setQty(productId: Long, qty: Int) {
        val item = items.find { it.product.id == productId } ?: return
        if (qty <= 0) items.remove(item) else item.quantity = qty
    }

    fun remove(productId: Long) = items.removeAll { it.product.id == productId }
    fun clear() = items.clear()
    fun totalItems(): Int = items.sumOf { it.quantity }
    fun totalPrice(): Double = items.sumOf { it.product.price * it.quantity }
}