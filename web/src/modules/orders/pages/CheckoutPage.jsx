// src/modules/orders/pages/CheckoutPage.jsx
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../../shared/components/Navbar.jsx";
import { useCart } from "../../cart/context/CartContext.jsx";
import { placeOrder } from "../api/ordersApi.js";
import { formatPHP } from "../../../shared/utils/format.js";
import "./CheckoutPage.css";

const PAYMENT_OPTIONS = [
  { value: "COD",           label: "Cash on Delivery", icon: "💵", desc: "Pay when your order arrives" },
  { value: "GCASH",         label: "GCash",            icon: "📱", desc: "Send to 09XX-XXX-XXXX (mock)" },
  { value: "MAYA",          label: "Maya (PayMaya)",   icon: "💳", desc: "Send to 09XX-XXX-XXXX (mock)" },
  { value: "BANK_TRANSFER", label: "Bank Transfer",    icon: "🏦", desc: "BDO / BPI / UnionBank (mock)" },
];

export default function CheckoutPage() {
  const { items, total, clearCart } = useCart();
  const navigate = useNavigate();

  const [form, setForm] = useState({ fullName: "", street: "", barangay: "", city: "", province: "", zipCode: "", phone: "", notes: "" });
  const [paymentMethod, setPaymentMethod] = useState("COD");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    document.title = "AquaHaven | Checkout";
    if (items.length === 0) navigate("/cart");
  }, [items, navigate]);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError("");
  };

  const buildAddress = () =>
    `${form.fullName}, ${form.street}, Brgy. ${form.barangay}, ${form.city}, ${form.province} ${form.zipCode} | Tel: ${form.phone}`;

  const handlePlaceOrder = async () => {
    if (!form.fullName || !form.street || !form.barangay || !form.city || !form.province || !form.zipCode || !form.phone) {
      setError("Please fill in all required fields.");
      return;
    }
    setLoading(true);
    setError("");
    try {
      const order = await placeOrder({
        items: items.map((i) => ({ productId: i.product.id, quantity: i.qty })),
        shippingAddress: buildAddress(),
        paymentMethod,
        notes: form.notes || undefined,
      });
      clearCart();
      navigate(`/orders/${order.orderRef}`, { state: { justPlaced: true } });
    } catch (err) {
      setError(err.message ?? "Failed to place order. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />
      <div className="checkout-page" style={{ marginTop: "var(--navbar-h)" }}>
        <div className="checkout-container">
          <h1 className="checkout-title">Checkout</h1>

          <div className="checkout-layout">
            <div className="checkout-form">
              <section className="checkout-section">
                <h2 className="checkout-section-title">🚚 Shipping Information</h2>
                <div className="co-field"><label>Full Name *</label><input name="fullName" value={form.fullName} onChange={handleChange} placeholder="Juan dela Cruz" /></div>
                <div className="co-field"><label>Street / House No. / Building *</label><input name="street" value={form.street} onChange={handleChange} placeholder="123 Rizal St." /></div>
                <div className="co-grid-2">
                  <div className="co-field"><label>Barangay *</label><input name="barangay" value={form.barangay} onChange={handleChange} placeholder="Lahug" /></div>
                  <div className="co-field"><label>City / Municipality *</label><input name="city" value={form.city} onChange={handleChange} placeholder="Cebu City" /></div>
                </div>
                <div className="co-grid-2">
                  <div className="co-field"><label>Province *</label><input name="province" value={form.province} onChange={handleChange} placeholder="Cebu" /></div>
                  <div className="co-field"><label>ZIP Code *</label><input name="zipCode" value={form.zipCode} onChange={handleChange} placeholder="6000" maxLength={10} /></div>
                </div>
                <div className="co-field"><label>Phone Number *</label><input name="phone" value={form.phone} onChange={handleChange} placeholder="09XX-XXX-XXXX" /></div>
                <div className="co-field">
                  <label>Order Notes (optional)</label>
                  <textarea name="notes" value={form.notes} onChange={handleChange} placeholder="Special delivery instructions…" rows={3} style={{ resize: "vertical" }} />
                </div>
              </section>

              <section className="checkout-section">
                <h2 className="checkout-section-title">💳 Payment Method</h2>
                <div className="payment-options">
                  {PAYMENT_OPTIONS.map((opt) => (
                    <label key={opt.value} className={`payment-opt ${paymentMethod === opt.value ? "payment-opt--active" : ""}`}>
                      <input type="radio" name="payment" checked={paymentMethod === opt.value} onChange={() => setPaymentMethod(opt.value)} style={{ display: "none" }} />
                      <span className="payment-opt__icon">{opt.icon}</span>
                      <div><strong>{opt.label}</strong><p>{opt.desc}</p></div>
                      {paymentMethod === opt.value && <span className="payment-opt__check">✓</span>}
                    </label>
                  ))}
                </div>
              </section>
            </div>

            <aside className="checkout-summary">
              <h2 className="checkout-summary__title">Order Summary</h2>
              <div className="checkout-items">
                {items.map((item) => (
                  <div key={item.product.id} className="checkout-item">
                    <div className="checkout-item__img">
                      {item.product.imageUrl
                        ? <img src={item.product.imageUrl} alt={item.product.name} onError={(e) => { e.currentTarget.style.display = "none"; }} />
                        : <span>{item.product.category.icon}</span>}
                    </div>
                    <div className="checkout-item__info">
                      <p className="checkout-item__name">{item.product.name}</p>
                      <p className="checkout-item__qty">Qty: {item.qty}</p>
                    </div>
                    <p className="checkout-item__price">{formatPHP(item.product.price * item.qty)}</p>
                  </div>
                ))}
              </div>

              <div className="checkout-summary__divider" />
              <div className="checkout-summary__row"><span>Subtotal ({items.reduce((s, i) => s + i.qty, 0)} items)</span><span>{formatPHP(total)}</span></div>
              <div className="checkout-summary__row"><span>Shipping</span><span className="checkout-summary__free">Free</span></div>
              <div className="checkout-summary__divider" />
              <div className="checkout-summary__total"><span>Total</span><span>{formatPHP(total)}</span></div>

              {error && <p className="checkout-error">{error}</p>}
              <button className="checkout-place-btn" onClick={handlePlaceOrder} disabled={loading}>
                {loading ? "Placing Order…" : "Place Order"}
              </button>
              <button className="checkout-back-btn" onClick={() => navigate("/cart")}>← Back to Cart</button>
            </aside>
          </div>
        </div>
      </div>
    </>
  );
}