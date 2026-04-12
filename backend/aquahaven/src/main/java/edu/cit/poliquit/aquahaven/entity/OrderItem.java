package edu.cit.poliquit.aquahaven.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    /** Snapshot of the price at time of order — never changes retroactively */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    // ── Getters & Setters ──────────────────────────────────────────────────────
    public Long getId()                      { return id; }
    public Order getOrder()                  { return order; }
    public void setOrder(Order o)            { this.order = o; }
    public Product getProduct()              { return product; }
    public void setProduct(Product p)        { this.product = p; }
    public int getQuantity()                 { return quantity; }
    public void setQuantity(int q)           { this.quantity = q; }
    public BigDecimal getUnitPrice()         { return unitPrice; }
    public void setUnitPrice(BigDecimal p)   { this.unitPrice = p; }
    public BigDecimal getSubtotal()          { return subtotal; }
    public void setSubtotal(BigDecimal s)    { this.subtotal = s; }
}