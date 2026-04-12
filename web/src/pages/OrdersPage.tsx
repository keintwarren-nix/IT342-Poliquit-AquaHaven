import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { fetchMyOrders } from "../services/productService";
import { useAuth } from "../context/AuthContext";
import type { Order, OrderStatus } from "../services/productService";
import "./OrdersPage.css";

const STATUS_CONFIG: Record<OrderStatus, { label: string; color: string; bg: string; icon: string }> = {
  PENDING:    { label: "Pending Payment", color: "#92400E", bg: "#FEF3C7", icon: "⏳" },
  PAID:       { label: "Paid",            color: "#1D4ED8", bg: "#DBEAFE", icon: "✅" },
  PROCESSING: { label: "Processing",      color: "#6D28D9", bg: "#EDE9FE", icon: "📦" },
  SHIPPED:    { label: "Shipped",         color: "#0E7490", bg: "#CFFAFE", icon: "🚚" },
  DELIVERED:  { label: "Delivered",       color: "#065F46", bg: "#D1FAE5", icon: "🎉" },
  CANCELLED:  { label: "Cancelled",       color: "#B91C1C", bg: "#FEE2E2", icon: "✕"  },
};

export default function OrdersPage() {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [orders,  setOrders]  = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error,   setError]   = useState("");

  useEffect(() => {
    document.title = "AquaHaven | My Orders";

    if (!isAuthenticated) {
      navigate("/login", { replace: true });
      return;
    }

    fetchMyOrders()
      .then(setOrders)
      .catch(() => setError("Could not load orders."))
      .finally(() => setLoading(false));
  }, [isAuthenticated, navigate]);

  return (
    <>
      <Navbar />
      <div className="orders-page" style={{ marginTop: 58 }}>
        <div className="orders-container">
          <div className="orders-header">
            <h1 className="orders-title">My Orders</h1>
            <Link to="/products" className="orders-shop-link">Continue Shopping →</Link>
          </div>

          {loading && (
            <div className="orders-state">
              <div className="orders-spinner" />
              <p>Loading your orders…</p>
            </div>
          )}

          {!loading && error && (
            <div className="orders-state orders-state--error">
              <span>⚠️</span><p>{error}</p>
            </div>
          )}

          {!loading && !error && orders.length === 0 && (
            <div className="orders-state">
              <span>📋</span>
              <h3>No orders yet</h3>
              <p>Your completed orders will appear here.</p>
              <Link to="/products" className="orders-browse-btn">Browse Products</Link>
            </div>
          )}

          {!loading && !error && orders.length > 0 && (
            <div className="orders-list">
              {orders.map(order => {
                const cfg = STATUS_CONFIG[order.status];
                const date = new Date(order.createdAt).toLocaleDateString("en-PH", {
                  year: "numeric", month: "long", day: "numeric"
                });
                return (
                  <div
                    key={order.id}
                    className="order-card"
                    onClick={() => navigate(`/orders/${order.orderRef}`)}
                    role="button" tabIndex={0}
                    onKeyDown={e => e.key === "Enter" && navigate(`/orders/${order.orderRef}`)}
                  >
                    <div className="order-card__top">
                      <div>
                        <p className="order-card__ref">{order.orderRef}</p>
                        <p className="order-card__date">{date}</p>
                      </div>
                      <span
                        className="order-card__status"
                        style={{ color: cfg.color, background: cfg.bg }}
                      >
                        {cfg.icon} {cfg.label}
                      </span>
                    </div>

                    <div className="order-card__items">
                      {order.items.slice(0, 3).map(item => (
                        <div key={item.productId} className="order-card__item">
                          <div className="order-card__item-img">
                            {item.productImageUrl ? (
                              <img src={item.productImageUrl} alt={item.productName}
                                onError={e => { (e.currentTarget as HTMLImageElement).style.display = "none"; }} />
                            ) : <span>🐠</span>}
                          </div>
                          <div className="order-card__item-info">
                            <p>{item.productName}</p>
                            <p>Qty: {item.quantity} × ₱{Number(item.unitPrice).toLocaleString("en-PH", { minimumFractionDigits: 2 })}</p>
                          </div>
                        </div>
                      ))}
                      {order.items.length > 3 && (
                        <p className="order-card__more">+{order.items.length - 3} more item(s)</p>
                      )}
                    </div>

                    <div className="order-card__footer">
                      <span className="order-card__payment">{order.paymentMethod.replace("_", " ")}</span>
                      <span className="order-card__total">
                        Total: <strong>₱{Number(order.totalAmount).toLocaleString("en-PH", { minimumFractionDigits: 2 })}</strong>
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </>
  );
}