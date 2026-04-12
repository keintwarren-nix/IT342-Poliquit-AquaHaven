package edu.cit.poliquit.aquahaven.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    public enum Status {
        PENDING,      // placed, awaiting payment
        PAID,         // payment confirmed
        PROCESSING,   // being prepared
        SHIPPED,      // on the way
        DELIVERED,    // completed
        CANCELLED
    }

    public enum PaymentMethod {
        COD,          // Cash on Delivery
        GCASH,
        MAYA,
        BANK_TRANSFER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Human-readable order reference e.g. AH-00001 */
    @Column(nullable = false, unique = true, length = 20)
    private String orderRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    // ── Getters & Setters ──────────────────────────────────────────────────────
    public Long getId()                          { return id; }
    public String getOrderRef()                  { return orderRef; }
    public void setOrderRef(String r)            { this.orderRef = r; }
    public User getUser()                        { return user; }
    public void setUser(User u)                  { this.user = u; }
    public List<OrderItem> getItems()            { return items; }
    public void setItems(List<OrderItem> items)  { this.items = items; }
    public BigDecimal getTotalAmount()           { return totalAmount; }
    public void setTotalAmount(BigDecimal t)     { this.totalAmount = t; }
    public Status getStatus()                    { return status; }
    public void setStatus(Status s)              { this.status = s; }
    public PaymentMethod getPaymentMethod()      { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod m){ this.paymentMethod = m; }
    public String getShippingAddress()           { return shippingAddress; }
    public void setShippingAddress(String a)     { this.shippingAddress = a; }
    public String getNotes()                     { return notes; }
    public void setNotes(String n)               { this.notes = n; }
    public LocalDateTime getCreatedAt()          { return createdAt; }
    public LocalDateTime getUpdatedAt()          { return updatedAt; }
}