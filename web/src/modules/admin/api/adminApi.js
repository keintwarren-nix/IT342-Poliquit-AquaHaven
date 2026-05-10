// src/modules/admin/api/adminApi.js
import httpClient from "../../../core/http/client.js";

// ── Products ──────────────────────────────────────────────────────────────────

export async function adminListProducts(page = 0, size = 20) {
  const res = await httpClient.get("/admin/products", { params: { page, size } });
  return res.data.data;
}

export async function adminCreateProduct(payload) {
  const res = await httpClient.post("/admin/products", payload);
  return res.data;
}

export async function adminUpdateProduct(id, payload) {
  const res = await httpClient.put(`/admin/products/${id}`, payload);
  return res.data;
}

export async function adminDeleteProduct(id) {
  const res = await httpClient.delete(`/admin/products/${id}`);
  return res.data;
}

// ── Orders ────────────────────────────────────────────────────────────────────

export async function adminListOrders(page = 0, size = 20, status = "") {
  const params = { page, size };
  if (status) params.status = status;
  const res = await httpClient.get("/admin/orders", { params });
  return res.data.data;
}

export async function adminGetOrder(ref) {
  const res = await httpClient.get(`/admin/orders/${ref}`);
  return res.data.data;
}

export async function adminUpdateOrderStatus(ref, status) {
  const res = await httpClient.patch(`/admin/orders/${ref}/status`, { status });
  return res.data;
}

export async function adminDeleteOrder(ref) {
  const res = await httpClient.delete(`/admin/orders/${ref}`);
  return res.data;
}

// ── Users ─────────────────────────────────────────────────────────────────────

export async function adminListUsers(page = 0, size = 20) {
  const res = await httpClient.get("/admin/users", { params: { page, size } });
  return res.data.data;
}

export async function adminDeleteUser(id) {
  const res = await httpClient.delete(`/admin/users/${id}`);
  return res.data;
}

export async function adminChangeUserRole(id, role) {
  const res = await httpClient.patch(`/admin/users/${id}/role`, { role });
  return res.data;
}