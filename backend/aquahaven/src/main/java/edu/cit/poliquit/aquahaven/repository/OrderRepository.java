package edu.cit.poliquit.aquahaven.repository;

import edu.cit.poliquit.aquahaven.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /** All orders for a given user, newest first */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    /** Lookup by order ref (e.g. AH-00042) and user — prevents peeking at others' orders */
    Optional<Order> findByOrderRefAndUserId(String orderRef, Long userId);

    /** For generating the next sequence number */
    @Query("SELECT COUNT(o) FROM Order o")
    long countAll();
}