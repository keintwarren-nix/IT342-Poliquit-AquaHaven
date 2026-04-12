package edu.cit.poliquit.aquahaven.dto;

import edu.cit.poliquit.aquahaven.entity.Order;
import edu.cit.poliquit.aquahaven.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {

    // ── Inbound: place an order ────────────────────────────────────────────────

    public static class PlaceOrderRequest {
        private List<CartItemRequest> items;
        private String shippingAddress;
        private String paymentMethod;   // COD | GCASH | MAYA | BANK_TRANSFER
        private String notes;

        public List<CartItemRequest> getItems()         { return items; }
        public void setItems(List<CartItemRequest> v)   { this.items = v; }
        public String getShippingAddress()              { return shippingAddress; }
        public void setShippingAddress(String v)        { this.shippingAddress = v; }
        public String getPaymentMethod()                { return paymentMethod; }
        public void setPaymentMethod(String v)          { this.paymentMethod = v; }
        public String getNotes()                        { return notes; }
        public void setNotes(String v)                  { this.notes = v; }
    }

    public static class CartItemRequest {
        private Long productId;
        private int  quantity;

        public Long getProductId()       { return productId; }
        public void setProductId(Long v) { this.productId = v; }
        public int  getQuantity()        { return quantity; }
        public void setQuantity(int v)   { this.quantity = v; }
    }

    // ── Outbound: order summary ───────────────────────────────────────────────

    public static class OrderResponse {
        private final Long             id;
        private final String           orderRef;
        private final String           status;
        private final String           paymentMethod;
        private final BigDecimal       totalAmount;
        private final String           shippingAddress;
        private final String           notes;
        private final LocalDateTime    createdAt;
        private final LocalDateTime    updatedAt;
        private final List<OrderItemResponse> items;

        private OrderResponse(Builder b) {
            this.id             = b.id;
            this.orderRef       = b.orderRef;
            this.status         = b.status;
            this.paymentMethod  = b.paymentMethod;
            this.totalAmount    = b.totalAmount;
            this.shippingAddress= b.shippingAddress;
            this.notes          = b.notes;
            this.createdAt      = b.createdAt;
            this.updatedAt      = b.updatedAt;
            this.items          = b.items;
        }

        public static OrderResponse from(Order o) {
            return new Builder()
                    .id(o.getId())
                    .orderRef(o.getOrderRef())
                    .status(o.getStatus().name())
                    .paymentMethod(o.getPaymentMethod().name())
                    .totalAmount(o.getTotalAmount())
                    .shippingAddress(o.getShippingAddress())
                    .notes(o.getNotes())
                    .createdAt(o.getCreatedAt())
                    .updatedAt(o.getUpdatedAt())
                    .items(o.getItems().stream()
                            .map(OrderItemResponse::from)
                            .collect(Collectors.toList()))
                    .build();
        }

        public Long             getId()             { return id; }
        public String           getOrderRef()       { return orderRef; }
        public String           getStatus()         { return status; }
        public String           getPaymentMethod()  { return paymentMethod; }
        public BigDecimal       getTotalAmount()    { return totalAmount; }
        public String           getShippingAddress(){ return shippingAddress; }
        public String           getNotes()          { return notes; }
        public LocalDateTime    getCreatedAt()      { return createdAt; }
        public LocalDateTime    getUpdatedAt()      { return updatedAt; }
        public List<OrderItemResponse> getItems()   { return items; }

        public static class Builder {
            private Long id; private String orderRef, status, paymentMethod, shippingAddress, notes;
            private BigDecimal totalAmount; private LocalDateTime createdAt, updatedAt;
            private List<OrderItemResponse> items;

            public Builder id(Long v)                      { id = v;             return this; }
            public Builder orderRef(String v)              { orderRef = v;       return this; }
            public Builder status(String v)                { status = v;         return this; }
            public Builder paymentMethod(String v)         { paymentMethod = v;  return this; }
            public Builder totalAmount(BigDecimal v)       { totalAmount = v;    return this; }
            public Builder shippingAddress(String v)       { shippingAddress = v;return this; }
            public Builder notes(String v)                 { notes = v;          return this; }
            public Builder createdAt(LocalDateTime v)      { createdAt = v;      return this; }
            public Builder updatedAt(LocalDateTime v)      { updatedAt = v;      return this; }
            public Builder items(List<OrderItemResponse> v){ items = v;          return this; }
            public OrderResponse build()                   { return new OrderResponse(this); }
        }
    }

    public static class OrderItemResponse {
        private final Long       productId;
        private final String     productName;
        private final String     productImageUrl;
        private final int        quantity;
        private final BigDecimal unitPrice;
        private final BigDecimal subtotal;

        private OrderItemResponse(OrderItem i) {
            this.productId      = i.getProduct().getId();
            this.productName    = i.getProduct().getName();
            this.productImageUrl= i.getProduct().getImageUrl();
            this.quantity       = i.getQuantity();
            this.unitPrice      = i.getUnitPrice();
            this.subtotal       = i.getSubtotal();
        }

        public static OrderItemResponse from(OrderItem i) { return new OrderItemResponse(i); }

        public Long       getProductId()       { return productId; }
        public String     getProductName()     { return productName; }
        public String     getProductImageUrl() { return productImageUrl; }
        public int        getQuantity()        { return quantity; }
        public BigDecimal getUnitPrice()       { return unitPrice; }
        public BigDecimal getSubtotal()        { return subtotal; }
    }
}