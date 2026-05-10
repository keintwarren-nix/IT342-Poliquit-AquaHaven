// src/modules/admin/pages/AdminOrdersPage.jsx
import { useEffect, useState, useCallback } from "react";
import AdminLayout from "../components/AdminLayout.jsx";
import {
  adminListOrders, adminUpdateOrderStatus, adminDeleteOrder,
} from "../api/adminApi.js";
import { formatPHP } from "../../../shared/utils/format.js";

const STATUSES = ["", "PENDING", "PAID", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"];

const STATUS_BADGE = {
  PENDING:    "pending",
  PAID:       "paid",
  PROCESSING: "processing",
  SHIPPED:    "shipped",
  DELIVERED:  "delivered",
  CANCELLED:  "cancelled",
};

export default function AdminOrdersPage() {
  const [orders, setOrders]             = useState([]);
  const [page, setPage]                 = useState(0);
  const [totalPages, setTotalPages]     = useState(0);
  const [statusFilter, setStatusFilter] = useState("");
  const [loading, setLoading]           = useState(false);
  const [error, setError]               = useState("");
  const [expanded, setExpanded]         = useState(null);

  useEffect(() => { document.title = "Admin | Orders"; }, []);

  const load = useCallback(() => {
    setLoading(true); setError("");
    adminListOrders(page, 20, statusFilter)
      .then((d) => { setOrders(d.content ?? []); setTotalPages(d.totalPages ?? 0); })
      .catch(() => setError("Failed to load orders."))
      .finally(() => setLoading(false));
  }, [page, statusFilter]);

  useEffect(() => { load(); }, [load]);

  const handleStatusChange = async (ref, status) => {
    const res = await adminUpdateOrderStatus(ref, status).catch(() => null);
    if (res?.success === false) { alert(res.message || "Failed to update."); return; }
    load();
  };

  const handleDelete = async (ref) => {
    if (!confirm(`Delete order ${ref}? This cannot be undone.`)) return;
    const res = await adminDeleteOrder(ref).catch(() => null);
    if (res?.success === false) { alert(res.message || "Delete failed."); return; }
    setExpanded(null); load();
  };

  return (
    <AdminLayout>
      <div className="adm-page-header">
        <div>
          <h1 className="adm-page-title">Orders</h1>
          <p className="adm-page-sub">Manage all customer orders</p>
        </div>
      </div>

      {error && <div className="adm-error-bar">{error}</div>}

      <div className="adm-card">
        <div className="adm-filter-bar">
          <span style={{ fontSize: "0.85rem", color: "#7A8278", fontWeight: 600 }}>Filter:</span>
          <select value={statusFilter} onChange={(e) => { setStatusFilter(e.target.value); setPage(0); }}>
            {STATUSES.map((s) => (
              <option key={s} value={s}>{s || "All Statuses"}</option>
            ))}
          </select>
        </div>

        {loading ? <div className="adm-spinner" /> : (
          <>
            <div className="adm-table-wrap">
              <table className="adm-table">
                <thead>
                  <tr>
                    <th>Ref</th><th>Customer</th><th>Total</th>
                    <th>Payment</th><th>Status</th><th>Date</th><th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.length === 0 ? (
                    <tr><td colSpan={7}>
                      <div className="adm-empty">
                        <div className="adm-empty__icon">📋</div>
                        <h3>No orders</h3>
                        <p>{statusFilter ? `No ${statusFilter} orders found.` : "No orders yet."}</p>
                      </div>
                    </td></tr>
                  ) : orders.map((o) => (
                    <>
                      <tr key={o.orderRef} style={{ cursor: "pointer" }} onClick={() => setExpanded(expanded === o.orderRef ? null : o.orderRef)}>
                        <td><strong>{o.orderRef}</strong></td>
                        <td>
                          <div style={{ fontSize: "0.88rem" }}>{o.userFullName || "—"}</div>
                          <div style={{ fontSize: "0.75rem", color: "#7A8278" }}>{o.userEmail}</div>
                        </td>
                        <td><strong>{formatPHP(o.totalAmount)}</strong></td>
                        <td style={{ fontSize: "0.82rem" }}>{o.paymentMethod}</td>
                        <td>
                          <span className={`adm-badge adm-badge--${STATUS_BADGE[o.status] ?? "pending"}`}>
                            {o.status}
                          </span>
                        </td>
                        <td style={{ fontSize: "0.82rem", color: "#7A8278" }}>
                          {new Date(o.createdAt).toLocaleDateString()}
                        </td>
                        <td onClick={(e) => e.stopPropagation()}>
                          <div style={{ display: "flex", gap: "0.4rem", alignItems: "center" }}>
                            <select
                              value={o.status}
                              onChange={(e) => handleStatusChange(o.orderRef, e.target.value)}
                              style={{ padding: "0.25rem 0.5rem", borderRadius: 6, border: "1px solid #E2DDD6", fontSize: "0.8rem", fontFamily: "'DM Sans',sans-serif" }}
                            >
                              {STATUSES.filter(Boolean).map((s) => (
                                <option key={s} value={s}>{s}</option>
                              ))}
                            </select>
                            <button className="adm-btn adm-btn--danger adm-btn--sm" onClick={() => handleDelete(o.orderRef)}>✕</button>
                          </div>
                        </td>
                      </tr>

                      {expanded === o.orderRef && (
                        <tr key={`${o.orderRef}-exp`}>
                          <td colSpan={7} style={{ background: "#FDFAF6", padding: "1rem 1.5rem" }}>
                            <strong style={{ fontSize: "0.8rem", color: "#7A8278", textTransform: "uppercase", letterSpacing: "0.05em" }}>Items</strong>
                            <table style={{ width: "100%", marginTop: "0.5rem", fontSize: "0.85rem" }}>
                              <thead>
                                <tr>
                                  <th style={{ textAlign: "left", padding: "0.3rem 0", color: "#7A8278", fontWeight: 600 }}>Product</th>
                                  <th style={{ textAlign: "right", padding: "0.3rem 0", color: "#7A8278", fontWeight: 600 }}>Qty</th>
                                  <th style={{ textAlign: "right", padding: "0.3rem 0", color: "#7A8278", fontWeight: 600 }}>Unit</th>
                                  <th style={{ textAlign: "right", padding: "0.3rem 0", color: "#7A8278", fontWeight: 600 }}>Subtotal</th>
                                </tr>
                              </thead>
                              <tbody>
                                {(o.items ?? []).map((it) => (
                                  <tr key={it.productId}>
                                    <td style={{ padding: "0.2rem 0" }}>{it.productName}</td>
                                    <td style={{ textAlign: "right", padding: "0.2rem 0" }}>{it.quantity}</td>
                                    <td style={{ textAlign: "right", padding: "0.2rem 0" }}>{formatPHP(it.unitPrice)}</td>
                                    <td style={{ textAlign: "right", padding: "0.2rem 0" }}><strong>{formatPHP(it.subtotal)}</strong></td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                            <div style={{ marginTop: "0.75rem", fontSize: "0.82rem", color: "#4A5248" }}>
                              <strong>Shipping:</strong> {o.shippingAddress}
                              {o.notes && <><br /><strong>Notes:</strong> {o.notes}</>}
                            </div>
                          </td>
                        </tr>
                      )}
                    </>
                  ))}
                </tbody>
              </table>
            </div>

            {totalPages > 1 && (
              <div className="adm-pagination">
                <button className="adm-pagination__btn" onClick={() => setPage(p => p - 1)} disabled={page === 0}>‹</button>
                {Array.from({ length: totalPages }, (_, i) => (
                  <button key={i} className={`adm-pagination__btn ${i === page ? "adm-pagination__btn--active" : ""}`} onClick={() => setPage(i)}>{i + 1}</button>
                ))}
                <button className="adm-pagination__btn" onClick={() => setPage(p => p + 1)} disabled={page >= totalPages - 1}>›</button>
              </div>
            )}
          </>
        )}
      </div>
    </AdminLayout>
  );
}