// src/modules/orders/api/ordersApi.js
import httpClient from "../../../core/http/client.js";

export async function placeOrder(req) {
  const res = await httpClient.post("/orders", req);
  if (!res.data.success) throw new Error(res.data.error?.message ?? "Order failed");
  return res.data.data;
}

export async function fetchMyOrders() {
  const res = await httpClient.get("/orders");
  return res.data.data;
}

export async function fetchOrder(ref) {
  const res = await httpClient.get(`/orders/${ref}`);
  return res.data.data;
}

export async function confirmPayment(ref) {
  const res = await httpClient.post(`/orders/${ref}/pay`);
  if (!res.data.success) throw new Error(res.data.error?.message ?? "Payment failed");
  return res.data.data;
}
