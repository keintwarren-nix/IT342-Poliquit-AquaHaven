import { useEffect, useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchOrder, confirmPayment } from "../services/productService";
import type { Order, OrderStatus } from "../services/productService";

const STATUS_CONFIG: Record<OrderStatus, { label: string; color: string; bg: string; icon: string; step: number }> = {
  PENDING:    { label: "Pending Payment", color: "#92400E", bg: "#FEF3C7", icon: "⏳", step: 1 },
  PAID:       { label: "Paid",            color: "#1D4ED8", bg: "#DBEAFE", icon: "✅", step: 2 },
  PROCESSING: { label: "Processing",      color: "#6D28D9", bg: "#EDE9FE", icon: "📦", step: 3 },
  SHIPPED:    { label: "Shipped",         color: "#0E7490", bg: "#CFFAFE", icon: "🚚", step: 4 },
  DELIVERED:  { label: "Delivered",       color: "#065F46", bg: "#D1FAE5", icon: "🎉", step: 5 },
  CANCELLED:  { label: "Cancelled",       color: "#B91C1C", bg: "#FEE2E2", icon: "✕",  step: 0 },
};

const STEPS = ["Order Placed", "Payment", "Processing", "Shipped", "Delivered"];

const PAYMENT_INSTRUCTIONS: Record<string, { title: string; detail: string }> = {
  COD:           { title: "Cash on Delivery", detail: "Prepare exact change upon delivery. Our rider will collect payment." },
  GCASH:         { title: "GCash Payment",    detail: "Send ₱[TOTAL] to 0917-XXX-XXXX (Mock GCash). Use ref: [ORDER_REF]." },
  MAYA:          { title: "Maya Payment",     detail: "Send ₱[TOTAL] to 0917-XXX-XXXX (Mock Maya). Use ref: [ORDER_REF]." },
  BANK_TRANSFER: { title: "Bank Transfer",    detail: "Transfer to BDO #0001-2345-6789. Reference: [ORDER_REF]. Send screenshot to orders@aquahaven.ph." },
};

export default function OrderDetailPage() {
  const { ref }    = useParams<{ ref: string }>();
  const navigate   = useNavigate();
  const location   = useLocation();
  const justPlaced = (location.state as any)?.justPlaced ?? false;

  const [order,   setOrder]   = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [paying,  setPaying]  = useState(false);
  const [error,   setError]   = useState("");
  const [payErr,  setPayErr]  = useState("");

  useEffect(() => {
    if (!ref) return;
    document.title = `AquaHaven | Order ${ref}`;
    fetchOrder(ref)
      .then(setOrder)
      .catch(() => setError("Order not found."))
      .finally(() => setLoading(false));
  }, [ref]);

  const handlePay = async () => {
    if (!order) return;
    setPaying(true); setPayErr("");
    try {
      const updated = await confirmPayment(order.orderRef);
      setOrder(updated);
    } catch (e: any) {
      setPayErr(e.message ?? "Payment failed.");
    } finally {
      setPaying(false);
    }
  };

  if (loading) return (
    <>
      <Navbar />
      <div className="od-state"><div className="od-spinner" /></div>
    </>
  );

  if (error || !order) return (
    <>
      <Navbar />
      <div className="od-state od-state--error">
        <span>😕</span><p>{error || "Order not found."}</p>
        <button onClick={() => navigate("/orders")}>My Orders</button>
      </div>
    </>
  );

  const cfg       = STATUS_CONFIG[order.status];
  const step      = cfg.step;
  const isPending = order.status === "PENDING";
  const isCancelled = order.status === "CANCELLED";
  const payInfo   = PAYMENT_INSTRUCTIONS[order.paymentMethod];
  const detail    = payInfo?.detail
    .replace("[TOTAL]", Number(order.totalAmount).toLocaleString("en-PH", { minimumFractionDigits: 2 }))
    .replace(/\[ORDER_REF\]/g, order.orderRef);

  return (
    <>
      <Navbar />
      <div className="od" style={{ marginTop: 58 }}>
        <div className="od-container">

          {/* Breadcrumb */}
          <nav className="od-breadcrumb">
            <button onClick={() => navigate("/orders")}>My Orders</button>
            <span>/</span>
            <span>{order.orderRef}</span>
          </nav>

          {/* Success banner */}
          {justPlaced && (
            <div className="od-success-banner">
              🎉 <strong>Order placed successfully!</strong> Your order reference is <strong>{order.orderRef}</strong>.
              {isPending && " Please complete payment to proceed."}
            </div>
          )}

          <div className="od-layout">

            {/* ── Left ─────────────────────────────────────────────────── */}
            <div>
              {/* Status header */}
              <div className="od-status-card">
                <div>
                  <p className="od-ref">{order.orderRef}</p>
                  <p className="od-date">
                    Placed on {new Date(order.createdAt).toLocaleDateString("en-PH", {
                      weekday: "long", year: "numeric", month: "long", day: "numeric"
                    })}
                  </p>
                </div>
                <span className="od-status-badge" style={{ color: cfg.color, background: cfg.bg }}>
                  {cfg.icon} {cfg.label}
                </span>
              </div>

              {/* Progress tracker */}
              {!isCancelled && (
                <div className="od-progress">
                  {STEPS.map((s, i) => (
                    <div key={s} className={`od-step ${i + 1 <= step ? "od-step--done" : ""} ${i + 1 === step ? "od-step--active" : ""}`}>
                      <div className="od-step__dot">{i + 1 <= step ? "✓" : i + 1}</div>
                      <p className="od-step__label">{s}</p>
                      {i < STEPS.length - 1 && <div className={`od-step__line ${i + 1 < step ? "od-step__line--done" : ""}`} />}
                    </div>
                  ))}
                </div>
              )}

              {/* Payment instructions */}
              {isPending && payInfo && (
                <div className="od-pay-instructions">
                  <h3>💳 {payInfo.title}</h3>
                  <p>{detail}</p>
                  {payErr && <p className="od-pay-err">{payErr}</p>}
                  <button
                    className="od-confirm-pay-btn"
                    onClick={handlePay}
                    disabled={paying}
                  >
                    {paying ? "Confirming…" : "Confirm Payment"}
                  </button>
                  <p className="od-pay-note">
                    * This is a simulated payment for demo purposes.
                  </p>
                </div>
              )}

              {/* Order items */}
              <div className="od-items">
                <h3 className="od-section-title">Items Ordered</h3>
                {order.items.map(item => (
                  <div key={item.productId} className="od-item">
                    <div className="od-item__img">
                      {item.productImageUrl ? (
                        <img src={item.productImageUrl} alt={item.productName}
                          onError={e => { (e.currentTarget as HTMLImageElement).style.display = "none"; }} />
                      ) : <span>🐠</span>}
                    </div>
                    <div className="od-item__info">
                      <button
                        className="od-item__name"
                        onClick={() => navigate(`/products/${item.productId}`)}
                      >
                        {item.productName}
                      </button>
                      <p className="od-item__unit">₱{Number(item.unitPrice).toLocaleString("en-PH", { minimumFractionDigits: 2 })} each</p>
                    </div>
                    <div className="od-item__right">
                      <p className="od-item__qty">× {item.quantity}</p>
                      <p className="od-item__subtotal">
                        ₱{Number(item.subtotal).toLocaleString("en-PH", { minimumFractionDigits: 2 })}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* ── Right: Summary ────────────────────────────────────────── */}
            <aside className="od-summary">
              <h3 className="od-section-title">Order Details</h3>

              <div className="od-detail-row">
                <span>Order Reference</span>
                <strong>{order.orderRef}</strong>
              </div>
              <div className="od-detail-row">
                <span>Payment Method</span>
                <strong>{order.paymentMethod.replace("_", " ")}</strong>
              </div>
              <div className="od-detail-row">
                <span>Status</span>
                <strong style={{ color: cfg.color }}>{cfg.label}</strong>
              </div>

              <div className="od-divider" />

              <div className="od-detail-row">
                <span>Subtotal</span>
                <span>₱{Number(order.totalAmount).toLocaleString("en-PH", { minimumFractionDigits: 2 })}</span>
              </div>
              <div className="od-detail-row">
                <span>Shipping</span>
                <span style={{ color: "#3D7A56", fontWeight: 600 }}>Free</span>
              </div>

              <div className="od-divider" />

              <div className="od-detail-row od-total-row">
                <span>Total</span>
                <strong>₱{Number(order.totalAmount).toLocaleString("en-PH", { minimumFractionDigits: 2 })}</strong>
              </div>

              <div className="od-divider" />

              <h4 className="od-ship-title">Shipping Address</h4>
              <p className="od-ship-addr">{order.shippingAddress}</p>

              {order.notes && (
                <>
                  <h4 className="od-ship-title">Notes</h4>
                  <p className="od-ship-addr">{order.notes}</p>
                </>
              )}

              <button className="od-back-btn" onClick={() => navigate("/orders")}>
                ← Back to Orders
              </button>
            </aside>
          </div>
        </div>
      </div>
    </>
  );
}