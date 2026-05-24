// src/modules/admin/pages/AdminDashboard.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AdminLayout from "../components/AdminLayout.jsx";
import { adminListProducts, adminListOrders, adminListUsers } from "../api/adminApi.js";

const STATUS_COLORS = {
  PENDING: { color: "#92400E", bg: "#FEF3C7" },
  PAID: { color: "#1D4ED8", bg: "#DBEAFE" },
  PROCESSING: { color: "#6D28D9", bg: "#EDE9FE" },
  SHIPPED: { color: "#0E7490", bg: "#CFFAFE" },
  DELIVERED: { color: "#065F46", bg: "#D1FAE5" },
  CANCELLED: { color: "#B91C1C", bg: "#FEE2E2" },
};

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [recentOrders, setRecentOrders] = useState([]);

  useEffect(() => {
    document.title = "Admin | Dashboard";
    Promise.all([
      adminListProducts(0, 1),
      adminListOrders(0, 1),
      adminListUsers(0, 1),
      adminListOrders(0, 1, "PENDING"),
      adminListOrders(0, 5),
    ]).then(([prods, orders, users, pending, recent]) => {
      setStats({
        products: prods.totalElements,
        orders: orders.totalElements,
        users: users.totalElements,
        pending: pending.totalElements,
      });
      setRecentOrders(recent.content || []);
    }).catch(() => {});
  }, []);

  return (
    <AdminLayout>
      <div className="adm-page-header">
        <div>
          <h1 className="adm-page-title">Dashboard</h1>
          <p className="adm-page-sub">Overview of your store</p>
        </div>
      </div>

      {stats ? (
        <>
          <div className="adm-stats">
            <div className="adm-stat-card">
              <div className="adm-stat-card__label">Products</div>
              <span className="adm-stat-card__icon">🐠</span>
              <div className="adm-stat-card__value">{stats.products}</div>
            </div>
            <div className="adm-stat-card">
              <div className="adm-stat-card__label">Total Orders</div>
              <span className="adm-stat-card__icon">📋</span>
              <div className="adm-stat-card__value">{stats.orders}</div>
            </div>
            <div className="adm-stat-card">
              <div className="adm-stat-card__label">Pending Orders</div>
              <span className="adm-stat-card__icon">⏳</span>
              <div className="adm-stat-card__value">{stats.pending}</div>
            </div>
            <div className="adm-stat-card">
              <div className="adm-stat-card__label">Registered Users</div>
              <span className="adm-stat-card__icon">👥</span>
              <div className="adm-stat-card__value">{stats.users}</div>
            </div>
          </div>

          <div className="adm-card">
            <div className="adm-card__header">
              <h2 className="adm-card__title">Recent Orders</h2>
              <Link to="/admin/orders" className="adm-btn adm-btn--ghost adm-btn--sm">View All</Link>
            </div>
            <div className="adm-table-wrap">
              <table className="adm-table">
                <thead>
                  <tr>
                    <th>Order Ref</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Total</th>
                  </tr>
                </thead>
                <tbody>
                  {recentOrders.map((order) => {
                    const cfg = STATUS_COLORS[order.status];
                    const date = new Date(order.createdAt).toLocaleDateString("en-PH", { year: "numeric", month: "short", day: "numeric" });
                    return (
                      <tr key={order.id}>
                        <td style={{ fontWeight: 600 }}>{order.orderRef}</td>
                        <td>{date}</td>
                        <td>
                          <span className="adm-badge" style={{ background: cfg.bg, color: cfg.color }}>
                            {order.status}
                          </span>
                        </td>
                        <td style={{ fontWeight: 600 }}>
                          ₱{Number(order.totalAmount).toLocaleString("en-PH", { minimumFractionDigits: 2 })}
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </>
      ) : (
        <div className="adm-spinner" />
      )}
    </AdminLayout>
  );
}