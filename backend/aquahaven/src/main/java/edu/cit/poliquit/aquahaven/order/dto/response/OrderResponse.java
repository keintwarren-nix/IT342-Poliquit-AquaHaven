package edu.cit.poliquit.aquahaven.order.dto.response;

import edu.cit.poliquit.aquahaven.order.entity.Order;
import edu.cit.poliquit.aquahaven.order.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long               id;
    private String             orderRef;
    private String             status;
    private String             paymentMethod;
    private BigDecimal         totalAmount;
    private String             shippingAddress;
    private String             notes;
    private LocalDateTime      createdAt;
    private LocalDateTime      updatedAt;
    private List<ItemResponse> items;

    private OrderResponse() {}

    public static OrderResponse from(Order order) {
        OrderResponse r = new OrderResponse();
        r.id              = order.getId();
        r.orderRef        = order.getOrderRef();
        r.status          = order.getStatus().name();
        r.paymentMethod   = order.getPaymentMethod().name();
        r.totalAmount     = order.getTotalAmount();
        r.shippingAddress = order.getShippingAddress();
        r.notes           = order.getNotes();
        r.createdAt       = order.getCreatedAt();
        r.updatedAt       = order.getUpdatedAt();
        r.items           = order.getItems().stream()
                                 .map(ItemResponse::from)
                                 .collect(Collectors.toList());
        return r;
    }

    public Long               getId()              { return id; }
    public String             getOrderRef()        { return orderRef; }
    public String             getStatus()          { return status; }
    public String             getPaymentMethod()   { return paymentMethod; }
    public BigDecimal         getTotalAmount()     { return totalAmount; }
    public String             getShippingAddress() { return shippingAddress; }
    public String             getNotes()           { return notes; }
    public LocalDateTime      getCreatedAt()       { return createdAt; }
    public LocalDateTime      getUpdatedAt()       { return updatedAt; }
    public List<ItemResponse> getItems()           { return items; }

    public static class ItemResponse {
        private Long       productId;
        private String     productName;
        private int        quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;

        private ItemResponse() {}

        public static ItemResponse from(OrderItem item) {
            ItemResponse r = new ItemResponse();
            r.productId   = item.getProduct().getId();
            r.productName = item.getProduct().getName();
            r.quantity    = item.getQuantity();
            r.unitPrice   = item.getUnitPrice();
            r.subtotal    = item.getSubtotal();
            return r;
        }

        public Long       getProductId()   { return productId; }
        public String     getProductName() { return productName; }
        public int        getQuantity()    { return quantity; }
        public BigDecimal getUnitPrice()   { return unitPrice; }
        public BigDecimal getSubtotal()    { return subtotal; }
    }
}