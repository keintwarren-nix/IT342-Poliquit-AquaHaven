package edu.cit.poliquit.aquahaven.cart.repository;

import edu.cit.poliquit.aquahaven.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
