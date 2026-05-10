// src/modules/admin/pages/AdminUsersPage.jsx
import { useEffect, useState, useCallback } from "react";
import AdminLayout from "../components/AdminLayout.jsx";
import { adminListUsers, adminDeleteUser, adminChangeUserRole } from "../api/adminApi.js";

export default function AdminUsersPage() {
  const [users, setUsers]           = useState([]);
  const [page, setPage]             = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading]       = useState(false);
  const [error, setError]           = useState("");

  useEffect(() => { document.title = "Admin | Users"; }, []);

  const load = useCallback(() => {
    setLoading(true); setError("");
    adminListUsers(page, 20)
      .then((d) => { setUsers(d.content ?? []); setTotalPages(d.totalPages ?? 0); })
      .catch(() => setError("Failed to load users."))
      .finally(() => setLoading(false));
  }, [page]);

  useEffect(() => { load(); }, [load]);

  const handleRoleChange = async (id, role) => {
    const res = await adminChangeUserRole(id, role).catch(() => null);
    if (res?.success === false) { alert(res.message || "Failed to update role."); return; }
    load();
  };

  const handleDelete = async (u) => {
    if (!confirm(`Delete account for "${u.email}"? This cannot be undone.`)) return;
    const res = await adminDeleteUser(u.id).catch(() => null);
    if (res?.success === false) { alert(res.message || "Delete failed."); return; }
    load();
  };

  return (
    <AdminLayout>
      <div className="adm-page-header">
        <div>
          <h1 className="adm-page-title">Users</h1>
          <p className="adm-page-sub">Manage customer accounts</p>
        </div>
      </div>

      {error && <div className="adm-error-bar">{error}</div>}

      <div className="adm-card">
        {loading ? <div className="adm-spinner" /> : (
          <>
            <div className="adm-table-wrap">
              <table className="adm-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Joined</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {users.length === 0 ? (
                    <tr><td colSpan={7}>
                      <div className="adm-empty">
                        <div className="adm-empty__icon">👥</div>
                        <h3>No users found</h3>
                        <p>Registered users will appear here.</p>
                      </div>
                    </td></tr>
                  ) : users.map((u) => (
                    <tr key={u.id}>
                      <td style={{ color: "#7A8278", fontSize: "0.8rem" }}>#{u.id}</td>
                      <td><strong>{u.firstname} {u.lastname}</strong></td>
                      <td style={{ fontSize: "0.85rem", color: "#4A5248" }}>{u.email}</td>
                      <td style={{ fontSize: "0.85rem", color: "#7A8278" }}>{u.phone || "—"}</td>
                      <td>
                        <span className={`adm-badge adm-badge--${u.role?.toLowerCase() === "admin" ? "admin" : "customer"}`}>
                          {u.role}
                        </span>
                      </td>
                      <td style={{ fontSize: "0.82rem", color: "#7A8278" }}>
                        {u.createdAt ? new Date(u.createdAt).toLocaleDateString() : "—"}
                      </td>
                      <td>
                        <div style={{ display: "flex", gap: "0.4rem", alignItems: "center" }}>
                          <select
                            value={u.role}
                            onChange={(e) => handleRoleChange(u.id, e.target.value)}
                            style={{ padding: "0.25rem 0.5rem", borderRadius: 6, border: "1px solid #E2DDD6", fontSize: "0.8rem", fontFamily: "'DM Sans',sans-serif" }}
                          >
                            <option value="CUSTOMER">CUSTOMER</option>
                            <option value="ADMIN">ADMIN</option>
                          </select>
                          <button
                            className="adm-btn adm-btn--danger adm-btn--sm"
                            onClick={() => handleDelete(u)}
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {totalPages > 1 && (
              <div className="adm-pagination">
                <button className="adm-pagination__btn" onClick={() => setPage(p => p - 1)} disabled={page === 0}>‹</button>
                {Array.from({ length: totalPages }, (_, i) => (
                  <button
                    key={i}
                    className={`adm-pagination__btn ${i === page ? "adm-pagination__btn--active" : ""}`}
                    onClick={() => setPage(i)}
                  >{i + 1}</button>
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