package edu.cit.poliquit.aquahaven.order.repository;

import edu.cit.poliquit.aquahaven.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order>     findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByOrderRefAndUserId(String orderRef, Long userId);

    @Query("SELECT COUNT(o) FROM Order o")
    long countAll();

    // ── Admin additions ───────────────────────────────────────────────────────
    Optional<Order>   findByOrderRef(String orderRef);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items oi LEFT JOIN FETCH oi.product LEFT JOIN FETCH o.user ORDER BY o.createdAt DESC")
    Page<Order> findAllWithItemsAndUser(Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items oi LEFT JOIN FETCH oi.product LEFT JOIN FETCH o.user WHERE o.status = :status ORDER BY o.createdAt DESC")
    Page<Order> findByStatusWithItemsAndUser(Order.Status status, Pageable pageable);
}