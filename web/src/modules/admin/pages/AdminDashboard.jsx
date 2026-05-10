// src/modules/admin/pages/AdminDashboard.jsx
import { useEffect, useState } from "react";
import AdminLayout from "../components/AdminLayout.jsx";
import { adminListProducts, adminListOrders, adminListUsers } from "../api/adminApi.js";

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    document.title = "Admin | Dashboard";
    Promise.all([
      adminListProducts(0, 1),
      adminListOrders(0, 1),
      adminListUsers(0, 1),
      adminListOrders(0, 1, "PENDING"),
    ]).then(([prods, orders, users, pending]) => {
      setStats({
        products: prods.totalElements,
        orders:   orders.totalElements,
        users:    users.totalElements,
        pending:  pending.totalElements,
      });
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
      ) : (
        <div className="adm-spinner" />
      )}

      <div style={{ color: "#7A8278", fontSize: "0.88rem" }}>
        Use the sidebar to manage products, orders, and users.
      </div>
    </AdminLayout>
  );
}