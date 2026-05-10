package edu.cit.poliquit.aquahaven.order.dto.request;

public class CartItemRequest {
    private Long productId;
    private int  quantity;

    public Long getProductId()            { return productId; }
    public void setProductId(Long id)     { this.productId = id; }
    public int  getQuantity()             { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}