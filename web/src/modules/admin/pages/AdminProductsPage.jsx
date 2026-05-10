// src/modules/admin/pages/AdminProductsPage.jsx
import { useEffect, useState, useCallback } from "react";
import AdminLayout from "../components/AdminLayout.jsx";
import {
  adminListProducts, adminCreateProduct,
  adminUpdateProduct, adminDeleteProduct,
} from "../api/adminApi.js";
import { fetchCategories } from "../../catalog/api/catalogApi.js";
import { formatPHP } from "../../../shared/utils/format.js";

const EMPTY_FORM = {
  name: "", description: "", price: "", stock: "",
  imageUrl: "", waterType: "", categoryId: "", active: true,
};

const WATER_TYPES = ["", "freshwater", "saltwater", "brackish"];

export default function AdminProductsPage() {
  const [products, setProducts]     = useState([]);
  const [categories, setCategories] = useState([]);
  const [page, setPage]             = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading]       = useState(false);
  const [error, setError]           = useState("");
  const [modal, setModal]           = useState(null); // null | "create" | "edit"
  const [editing, setEditing]       = useState(null);
  const [form, setForm]             = useState(EMPTY_FORM);
  const [saving, setSaving]         = useState(false);
  const [formError, setFormError]   = useState("");

  useEffect(() => { document.title = "Admin | Products"; }, []);

  useEffect(() => {
    fetchCategories().then(setCategories).catch(() => {});
  }, []);

  const load = useCallback(() => {
    setLoading(true);
    setError("");
    adminListProducts(page, 20)
      .then((d) => { setProducts(d.content ?? []); setTotalPages(d.totalPages ?? 0); })
      .catch(() => setError("Failed to load products."))
      .finally(() => setLoading(false));
  }, [page]);

  useEffect(() => { load(); }, [load]);

  const openCreate = () => { setForm(EMPTY_FORM); setFormError(""); setModal("create"); };
  const openEdit   = (p) => {
    setEditing(p);
    setForm({
      name: p.name, description: p.description ?? "",
      price: String(p.price), stock: String(p.stock),
      imageUrl: p.imageUrl ?? "", waterType: p.waterType ?? "",
      categoryId: String(p.category?.id ?? p.categoryId ?? ""), active: p.active ?? true,
    });
    setFormError("");
    setModal("edit");
  };
  const closeModal = () => { setModal(null); setEditing(null); };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((f) => ({ ...f, [name]: type === "checkbox" ? checked : value }));
  };

  const buildPayload = () => ({
    name:        form.name.trim(),
    description: form.description.trim(),
    price:       parseFloat(form.price),
    stock:       parseInt(form.stock, 10),
    imageUrl:    form.imageUrl.trim() || null,
    waterType:   form.waterType || null,
    categoryId:  Number(form.categoryId),
    active:      form.active,
  });

  const handleSave = async () => {
    if (!form.name || !form.price || !form.stock || !form.categoryId) {
      setFormError("Name, price, stock and category are required.");
      return;
    }
    setSaving(true); setFormError("");
    try {
      const res = modal === "create"
        ? await adminCreateProduct(buildPayload())
        : await adminUpdateProduct(editing.id, buildPayload());
      if (!res.success) { setFormError(res.message || "Save failed."); return; }
      closeModal(); load();
    } catch { setFormError("Save failed. Check your input."); }
    finally { setSaving(false); }
  };

  const handleDelete = async (p) => {
    if (!confirm(`Delete "${p.name}"? This cannot be undone.`)) return;
    const res = await adminDeleteProduct(p.id).catch(() => null);
    if (res?.success === false) { alert(res.message || "Delete failed."); return; }
    load();
  };

  return (
    <AdminLayout>
      <div className="adm-page-header">
        <div>
          <h1 className="adm-page-title">Products</h1>
          <p className="adm-page-sub">{products.length > 0 ? `${products.length} shown` : ""}</p>
        </div>
        <button className="adm-btn adm-btn--primary" onClick={openCreate}>+ New Product</button>
      </div>

      {error && <div className="adm-error-bar">{error}</div>}

      <div className="adm-card">
        {loading ? <div className="adm-spinner" /> : (
          <>
            <div className="adm-table-wrap">
              <table className="adm-table">
                <thead>
                  <tr>
                    <th>ID</th><th>Name</th><th>Category</th>
                    <th>Price</th><th>Stock</th><th>Status</th><th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {products.length === 0 ? (
                    <tr><td colSpan={7}>
                      <div className="adm-empty">
                        <div className="adm-empty__icon">🐠</div>
                        <h3>No products yet</h3>
                        <p>Click "New Product" to add one.</p>
                      </div>
                    </td></tr>
                  ) : products.map((p) => (
                    <tr key={p.id}>
                      <td style={{ color: "#7A8278", fontSize: "0.8rem" }}>#{p.id}</td>
                      <td><strong>{p.name}</strong></td>
                      <td>{p.category?.name ?? p.categoryName ?? "—"}</td>
                      <td>{formatPHP(p.price)}</td>
                      <td style={{ color: p.stock < 5 ? "#B91C1C" : "inherit" }}>{p.stock}</td>
                      <td>
                        <span className={`adm-badge adm-badge--${p.active ? "active" : "inactive"}`}>
                          {p.active ? "Active" : "Inactive"}
                        </span>
                      </td>
                      <td>
                        <div style={{ display: "flex", gap: "0.4rem" }}>
                          <button className="adm-btn adm-btn--ghost adm-btn--sm" onClick={() => openEdit(p)}>Edit</button>
                          <button className="adm-btn adm-btn--danger adm-btn--sm" onClick={() => handleDelete(p)}>Delete</button>
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

      {/* Modal */}
      {modal && (
        <div className="adm-modal-overlay" onClick={closeModal}>
          <div className="adm-modal" onClick={(e) => e.stopPropagation()}>
            <div className="adm-modal__header">
              <h2 className="adm-modal__title">{modal === "create" ? "New Product" : "Edit Product"}</h2>
              <button className="adm-modal__close" onClick={closeModal}>✕</button>
            </div>

            {formError && <div className="adm-error-bar">{formError}</div>}

            <div className="adm-form">
              <div className="adm-field">
                <label>Product Name *</label>
                <input name="name" value={form.name} onChange={handleChange} placeholder="e.g. Neon Tetra" />
              </div>

              <div className="adm-field">
                <label>Description</label>
                <textarea name="description" value={form.description} onChange={handleChange} placeholder="Short product description…" />
              </div>

              <div className="adm-form-row">
                <div className="adm-field">
                  <label>Price (₱) *</label>
                  <input name="price" type="number" min="0" step="0.01" value={form.price} onChange={handleChange} placeholder="0.00" />
                </div>
                <div className="adm-field">
                  <label>Stock *</label>
                  <input name="stock" type="number" min="0" value={form.stock} onChange={handleChange} placeholder="0" />
                </div>
              </div>

              <div className="adm-form-row">
                <div className="adm-field">
                  <label>Category *</label>
                  <select name="categoryId" value={form.categoryId} onChange={handleChange}>
                    <option value="">— Select —</option>
                    {categories.map((c) => (
                      <option key={c.id} value={c.id}>{c.icon} {c.name}</option>
                    ))}
                  </select>
                </div>
                <div className="adm-field">
                  <label>Water Type</label>
                  <select name="waterType" value={form.waterType} onChange={handleChange}>
                    {WATER_TYPES.map((w) => (
                      <option key={w} value={w}>{w || "— None —"}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="adm-field">
                <label>Image URL</label>
                <input name="imageUrl" value={form.imageUrl} onChange={handleChange} placeholder="https://…" />
              </div>

              <label style={{ display: "flex", alignItems: "center", gap: "0.5rem", fontSize: "0.88rem", cursor: "pointer" }}>
                <input type="checkbox" name="active" checked={form.active} onChange={handleChange} />
                Active (visible in storefront)
              </label>

              <div className="adm-form-actions">
                <button className="adm-btn adm-btn--ghost" onClick={closeModal}>Cancel</button>
                <button className="adm-btn adm-btn--primary" onClick={handleSave} disabled={saving}>
                  {saving ? "Saving…" : modal === "create" ? "Create Product" : "Save Changes"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </AdminLayout>
  );
}