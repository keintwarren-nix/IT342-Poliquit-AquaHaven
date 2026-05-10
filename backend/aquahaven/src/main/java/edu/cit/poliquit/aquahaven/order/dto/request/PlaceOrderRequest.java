package edu.cit.poliquit.aquahaven.order.dto.request;

import java.util.List;

public class PlaceOrderRequest {
    private List<CartItemRequest> items;
    private String                shippingAddress;
    private String                paymentMethod;
    private String                notes;

    public List<CartItemRequest> getItems()                        { return items; }
    public void                  setItems(List<CartItemRequest> i) { this.items = i; }
    public String                getShippingAddress()              { return shippingAddress; }
    public void                  setShippingAddress(String a)      { this.shippingAddress = a; }
    public String                getPaymentMethod()                { return paymentMethod; }
    public void                  setPaymentMethod(String m)        { this.paymentMethod = m; }
    public String                getNotes()                        { return notes; }
    public void                  setNotes(String n)                { this.notes = n; }
}